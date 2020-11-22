package vn.prostylee.core.validator;

import vn.prostylee.core.exception.ValidatingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Component
@Slf4j
public class UniqueEntityValidator implements ConstraintValidator<UniqueEntity, UniqueIdentifier> {

   private final ApplicationContext applicationContext;

   private EntityExists service;

   private String[] fieldNames;

   @Autowired
   public UniqueEntityValidator(ApplicationContext applicationContext) {
      this.applicationContext = applicationContext;
   }

   @Override
   public void initialize(UniqueEntity unique) {
      Class<? extends EntityExists> clazz = unique.service();
      this.fieldNames = unique.fieldNames();
      String serviceQualifier = unique.serviceQualifier();

      if (StringUtils.isNotBlank(serviceQualifier)) {
         this.service = BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getAutowireCapableBeanFactory(), clazz, serviceQualifier.trim());
      } else {
         this.service = this.applicationContext.getBean(clazz);
      }
   }

   @Override
   public boolean isValid(UniqueIdentifier entity, ConstraintValidatorContext constraintValidatorContext) {
      log.info("UniqueEntityValidator " + entity);
      Map<String, Object> uniqueValues = new HashMap<>();
      Stream.of(fieldNames).forEach(fieldName -> uniqueValues.put(fieldName, this.getValue(entity, fieldName)));
      return !service.isEntityExists(entity.getId(), uniqueValues);
   }

   private Object getValue(UniqueIdentifier entity, String fieldName) {
      try {
         return FieldUtils.readField(entity, fieldName, true);
      } catch (IllegalAccessException e) {
         throw new ValidatingException("Can not read value of filename " + fieldName + " in object " + entity, e);
      }
   }
}