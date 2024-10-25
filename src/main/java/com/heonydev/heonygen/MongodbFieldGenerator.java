package com.heonydev.heonygen;

import org.springframework.data.annotation.Id;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;


import java.lang.annotation.Annotation;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MongodbFieldGenerator {

    // 지정된 패키지에서 모든 클래스를 찾고 필드 상수 파일을 생성하는 메서드
    public void generateFields(GeneratorConfig config) {
        try {
            // 대상 디렉토리 및 패키지 경로 설정
            String targetDir = config.target.directory;
            String targetPackage = config.target.packageName;
            File outputDir = new File(targetDir, targetPackage.replace(".", "/"));
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // 대상 패키지에서 모든 클래스를 검색하여 처리
            Set<Class<?>> targetClasses = findClasses(config.origin.packageName, config.origin.annotation);

            // 각 클래스에 대한 상수 파일 생성
            for (Class<?> clazz : targetClasses) {
                generateFieldClassFile(clazz, outputDir, targetPackage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Reflections 라이브러리를 사용하여 패키지 내 어노테이션이 붙은 클래스를 검색하는 메서드
    private Set<Class<?>> findClasses(String packageName, String annotation) throws ClassNotFoundException {
        // Reflections 설정: 주어진 패키지에서 클래스 검색
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))  // 검색할 패키지 설정
                .setScanners(Scanners.TypesAnnotated)             // 어노테이션을 기준으로 검색
                .filterInputsBy(new FilterBuilder().includePackage(packageName)));  // 패키지 필터 설정

        // 어노테이션 클래스를 로드
        Class<? extends Annotation> annotationClass = (Class<? extends Annotation>) Class.forName(annotation);

        // 주어진 어노테이션이 붙은 모든 클래스를 검색
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(annotationClass)
                .stream()
                .filter(clazz -> clazz.getPackage().getName().startsWith(packageName))  // 패키지 필터링
                .collect(Collectors.toSet());

        return annotatedClasses;
    }

    // 클래스의 필드를 상수화하여 Java 파일로 생성하는 메서드
    private void generateFieldClassFile(Class<?> clazz, File outputDir, String targetPackage) {
        try (FileWriter writer = new FileWriter(new File(outputDir, clazz.getSimpleName() + "Fields.java"))) {
            // 패키지 및 클래스 선언
            writer.write("package " + targetPackage + ";\n\n");
            writer.write("@SuppressWarnings({ \"all\", \"unchecked\", \"rawtypes\", \"this-escape\" })\n");
            writer.write("public class " + clazz.getSimpleName() + "Fields {\n");

            // 각 필드에 대해 상수 생성
            for (Field field : clazz.getDeclaredFields()) {
                String fieldName = field.getName();
                String constantName = fieldName.toUpperCase();

                // @Id 어노테이션이 있는 필드는 "_id"로 정의
                if (field.isAnnotationPresent(Id.class)) {
                    writer.write("    public static final String " + constantName + " = \"_id\";\n");
                } else {
                    writer.write("    public static final String " + constantName + " = \"" + fieldName + "\";\n");
                }
            }

            writer.write("}\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}