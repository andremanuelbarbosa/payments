package com.andremanuelbarbosa.payments.dao.jdbi.binder;

import org.skife.jdbi.v2.SQLStatement;
import org.skife.jdbi.v2.sqlobject.Binder;
import org.skife.jdbi.v2.sqlobject.BinderFactory;
import org.skife.jdbi.v2.sqlobject.BindingAnnotation;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.*;
import java.lang.reflect.Method;

@BindingAnnotation(BindCompositeBean.SomethingBinderFactory.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface BindCompositeBean {

    class SomethingBinderFactory implements BinderFactory {

        @Override
        public Binder build(Annotation annotation) {

            return new Binder<BindCompositeBean, Object>() {

                private void bind(SQLStatement sqlStatement, BindCompositeBean bindCompositeBean, Object o, String prefix) {

                    try {

                        final PropertyDescriptor[] props = Introspector.getBeanInfo(o.getClass()).getPropertyDescriptors();

                        for (PropertyDescriptor prop : props) {

                            final Method readMethod = prop.getReadMethod();

                            if (readMethod != null) {

                                final Object propValue = readMethod.invoke(o);
                                final Class<?> propType = readMethod.getReturnType();

                                sqlStatement.dynamicBind(propType, prefix + prop.getName(), propValue);

                                if (propType.isPrimitive()) {

                                    if (propValue instanceof Boolean) {

                                        sqlStatement.bind(prefix + prop.getName(), (boolean) propValue ? 1 : 0);
                                    }

                                } else if (!propType.getPackage().getName().equals("java.lang")) {

                                    final PropertyDescriptor[] compositeProps = Introspector.getBeanInfo(propType).getPropertyDescriptors();

                                    for (PropertyDescriptor compositeProp : compositeProps) {

                                        final Method compositeReadMethod = compositeProp.getReadMethod();

                                        if (compositeReadMethod != null) {

                                            final Class<?> compositePropType = compositeReadMethod.getReturnType();

                                            sqlStatement.dynamicBind(compositeReadMethod.getReturnType(), prefix + prop.getName() + "." + compositeProp.getName(), compositeReadMethod.invoke(propValue));

                                            if (!compositePropType.isPrimitive() && !compositePropType.getPackage().getName().equals("java.lang")) {

                                                bind(sqlStatement, bindCompositeBean, compositeReadMethod.invoke(propValue), prefix + prop.getName() + "." + compositeProp.getName() + ".");
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    } catch (Exception e) {

                        throw new IllegalArgumentException(e);
                    }
                }

                @Override
                public void bind(SQLStatement sqlStatement, BindCompositeBean bindCompositeBean, Object o) {

                    bind(sqlStatement, bindCompositeBean, o, "");

//                    try {
//
//                        final PropertyDescriptor[] props = Introspector.getBeanInfo(o.getClass()).getPropertyDescriptors();
//
//                        for (PropertyDescriptor prop : props) {
//
//                            final Method readMethod = prop.getReadMethod();
//
//                            if (readMethod != null) {
//
//                                final Object propValue = readMethod.invoke(o);
//                                final Class<?> propType = readMethod.getReturnType();
//
//                                sqlStatement.dynamicBind(propType, prop.getName(), propValue);
//
//                                if (propType.isPrimitive()) {
//
//                                    if (propValue instanceof Boolean) {
//
//                                        sqlStatement.bind(prop.getName(), (boolean) propValue ? 1 : 0);
//                                    }
//
//                                } else if (!propType.getPackage().getName().equals("java.lang")) {
//
//                                    final PropertyDescriptor[] compositeProps = Introspector.getBeanInfo(propType).getPropertyDescriptors();
//
//                                    for (PropertyDescriptor compositeProp : compositeProps) {
//
//                                        final Method compositeReadMethod = compositeProp.getReadMethod();
//
//                                        if (compositeReadMethod != null) {
//
//                                            sqlStatement.dynamicBind(compositeReadMethod.getReturnType(), prop.getName() + "." + compositeProp.getName(), compositeReadMethod.invoke(propValue));
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                    } catch (Exception e) {
//
//                        throw new IllegalArgumentException(e);
//                    }
                }
            };
        }
    }
}
