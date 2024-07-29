package com.example.springsecurity6.controller.user.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordConstraintValidator.class) // Hangi validator sinfi ilə doğrulama aparılacaq
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER }) // Hansı elementlərdə istifadə oluna bilər
@Retention(RetentionPolicy.RUNTIME) // Runtime zamanı annotasiya mövcud olacaq
public @interface ValidPassword {
    String message() default "Invalid password"; // Parol uyğun gəlmədikdə göstəriləcək mesaj
    Class<?>[] groups() default {}; // Qruplar (isteğe bağlı)
    Class<? extends Payload>[] payload() default {}; // Payload məlumatı (isteğe bağlı)
}
