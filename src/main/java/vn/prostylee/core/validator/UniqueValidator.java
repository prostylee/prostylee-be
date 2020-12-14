package vn.prostylee.core.validator;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
@RequiredArgsConstructor
public class UniqueValidator implements ConstraintValidator<Unique, Object> {

   private final ApplicationContext applicationContext;

   private FieldValueExists service;

   private String fieldName;

   @Override
   public void initialize(Unique unique) {
      Class<? extends FieldValueExists> clazz = unique.service();
      this.fieldName = unique.fieldName();
      String serviceQualifier = unique.serviceQualifier();

      if (StringUtils.isNotBlank(serviceQualifier)) {
         this.service = BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getAutowireCapableBeanFactory(), clazz, serviceQualifier.trim());
      } else {
         this.service = this.applicationContext.getBean(clazz);
      }
   }

   @Override
   public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
      return !this.service.isFieldValueExists(this.fieldName, value);
   }
}