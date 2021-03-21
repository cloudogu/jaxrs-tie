package com.github.sdorra.jaxrstie;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;

class SourceCodeGenerator {

  private static final String FIELD_URI_INFO = "uriInfo";
  private static final String FIELD_BUILDER = "builder";
  private static final String STATEMENT_FIELD_ASSIGNMENT = "this.$N = $N";

  void generate(Writer writer, Model model) throws IOException {
    MethodSpec constructor = MethodSpec.constructorBuilder()
      .addModifiers(Modifier.PUBLIC)
      .addParameter(UriInfo.class, FIELD_URI_INFO)
      .addStatement(STATEMENT_FIELD_ASSIGNMENT, FIELD_URI_INFO, FIELD_URI_INFO)
      .build();

    TypeSpec.Builder builder = TypeSpec.classBuilder(model.getSimpleClassName())
      .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
      .addField(UriInfo.class, FIELD_URI_INFO, Modifier.PRIVATE, Modifier.FINAL)
      .addMethod(constructor);

    for (RootResource rootResource : model.getRootResources()) {
      addResource(builder, rootResource);
    }

    JavaFile javaFile = JavaFile.builder(model.getPackageName(), builder.build()).build();
    javaFile.writeTo(writer);
  }

  private void addResource(TypeSpec.Builder parent, Resource resource) {
    ClassName className = ClassName.get("", resource.getClassName() + "Links");
    TypeSpec.Builder resourceSpec = createResourceSpec(resource, className);
    for (SubResource subResource : resource.getSubResources()) {
      ClassName subClassName = ClassName.get("", subResource.getClassName() + "Links");
      TypeSpec.Builder subResourceSpec = createResourceSpec(subResource, subClassName);

      resourceSpec.addType(subResourceSpec.build());

      String constructorParameters = "";
      if (!subResource.getParameters().isEmpty()) {
        constructorParameters = ", " + subResource.getAllParameters().getNames();
      }

      MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(subResource.getMethodName())
        .addModifiers(Modifier.PUBLIC)
        .returns(subClassName)
        .addStatement(
          "return new $T($N.path($T.class, $S)$N)",
          subClassName,
          FIELD_BUILDER,
          ClassName.bestGuess(resource.getType()),
          subResource.getName(),
          constructorParameters
        );

      for (MethodParameter parameter : subResource.getParameters()) {
        methodBuilder.addParameter(
          ClassName.bestGuess(parameter.getType()), parameter.getName()
        );
      }

      resourceSpec.addMethod(methodBuilder.build());
    }

    parent.addType(resourceSpec.build());

    MethodSpec methodSpec = MethodSpec.methodBuilder(resource.getMethodName())
      .addModifiers(Modifier.PUBLIC)
      .returns(className)
      .addStatement("return new $T($N.getBaseUriBuilder().path($T.class))", className, FIELD_URI_INFO, ClassName.bestGuess(resource.getType()))
      .build();

    parent.addMethod(methodSpec);
  }

  private TypeSpec.Builder createResourceSpec(Resource resource, ClassName className) {
    MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
      .addModifiers(Modifier.PRIVATE)
      .addParameter(UriBuilder.class, FIELD_BUILDER)
      .addStatement(STATEMENT_FIELD_ASSIGNMENT, FIELD_BUILDER, FIELD_BUILDER);

    for (MethodParameter parameter : resource.getAllParametersAnonymized()) {
      constructorBuilder.addParameter(
        ClassName.bestGuess(parameter.getType()), parameter.getName()
      );
      constructorBuilder.addStatement(
        STATEMENT_FIELD_ASSIGNMENT,
        parameter.getName(),
        parameter.getName()
      );
    }

    TypeSpec.Builder resourceBuilder = TypeSpec.classBuilder(className)
      .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
      .addMethod(constructorBuilder.build())
      .addField(UriBuilder.class, FIELD_BUILDER, Modifier.PRIVATE, Modifier.FINAL);

    for (MethodParameter parameter : resource.getAllParametersAnonymized()) {
      resourceBuilder.addField(
        ClassName.bestGuess(parameter.getType()), parameter.getName(), Modifier.PRIVATE, Modifier.FINAL
      );
    }

    for (Endpoint endpoint : resource.getEndpoints()) {
      MethodSpec.Builder endpointMethod = MethodSpec.methodBuilder(endpoint.getName())
        .addModifiers(Modifier.PUBLIC)
        .returns(URI.class);

      for (MethodParameter parameter : endpoint.getParameters()) {
        endpointMethod.addParameter(
          ClassName.bestGuess(parameter.getType()), parameter.getName()
        );
      }

      endpointMethod.addStatement(
        "return $N.path($T.class, $S).build($L)",
        FIELD_BUILDER,
        ClassName.bestGuess(resource.getType()),
        endpoint.getName(),
        endpoint.getAllParameters().getNames()
      );

      resourceBuilder.addMethod(endpointMethod.build());
    }

    return resourceBuilder;
  }


}
