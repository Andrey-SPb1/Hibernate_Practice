package com.andrey;

import com.andrey.dao.CompanyRepository;
import com.andrey.dao.UserRepository;
import com.andrey.dto.UserCreateDto;
import com.andrey.entity.PersonalInfo;
import com.andrey.entity.Role;
import com.andrey.interceptor.TransactionInterceptor;
import com.andrey.mapper.CompanyReadMapper;
import com.andrey.mapper.UserCreateMapper;
import com.andrey.mapper.UserReadMapper;
import com.andrey.service.UserService;
import com.andrey.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.time.LocalDate;

@Slf4j
public class HibernateRunner {

    @Transactional
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                    new Class[]{Session.class},
                    (proxy, method, args1) -> method.invoke(sessionFactory.getCurrentSession(), args1));
//            session.beginTransaction();

            CompanyRepository companyRepository = new CompanyRepository(session);

            UserCreateMapper userCreateMapper = new UserCreateMapper(companyRepository);
            UserRepository userRepository = new UserRepository(session);
            CompanyReadMapper companyReadMapper = new CompanyReadMapper();
            UserReadMapper userReadMapper = new UserReadMapper(companyReadMapper);
            TransactionInterceptor transactionInterceptor = new TransactionInterceptor(sessionFactory);

//            UserService userService = new UserService(userRepository, userReadMapper, userCreateMapper);

            UserService userService = new ByteBuddy()
                    .subclass(UserService.class)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(transactionInterceptor))
                    .make()
                    .load(UserService.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor(UserRepository.class, UserReadMapper.class, UserCreateMapper.class)
                    .newInstance(userRepository, userReadMapper, userCreateMapper);

            UserCreateDto userCreateDto = new UserCreateDto(
                    PersonalInfo.builder()
                            .firstname("Maria")
                            .lastname("Ivanova")
                            .birthDate(LocalDate.now())
                            .build(),
                    "maria3@gmail.com",
                    Role.USER,
                    1);

            userService.create(userCreateDto);

//            session.getTransaction().commit();
        }
    }

}
