package com.andrey.dao;

import com.andrey.dto.CompanyDto;
import com.andrey.entity.*;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.criteria.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDao {

    private static final UserDao INSTANCE = new UserDao();

    /**
     * Возвращает всех сотрудников
     */
    public List<User> findAll(Session session) {
//        return session.createQuery("select u from User u", User.class)
//                .list();

        HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

        JpaCriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
        JpaRoot<User> user = criteria.from(User.class);

        criteria.select(user);

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает всех сотрудников с указанным именем
     */
    public List<User> findAllByFirstName(Session session, String firstName) {
//        return session.createQuery("select u from User u where u.personalInfo.firstname = :firstname ", User.class)
//                .setParameter("firstname", firstName)
//                .list();

        HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

        JpaCriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
        JpaRoot<User> user = criteria.from(User.class);

        criteria.select(user)
                .where(criteriaBuilder.equal(user.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName));

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает первые {limit} сотрудников, упорядоченных по дате рождения (в порядке возрастания)
     */
    public List<User> findLimitedUsersOrderedByBirthday(Session session, int limit) {
//        return session.createQuery("select u from User u " +
//                        "order by u.personalInfo.birthDate", User.class)
//                .setMaxResults(limit)
//                .list();


        HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

        JpaCriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
        JpaRoot<User> user = criteria.from(User.class);

        criteria.select(user)
                .orderBy(criteriaBuilder.asc(user.get(User_.personalInfo).get(PersonalInfo_.birthDate)));

        return session.createQuery(criteria)
                .setMaxResults(limit)
                .list();
    }

    /**
     * Возвращает всех сотрудников компании с указанным названием
     */
    public List<User> findAllByCompanyName(Session session, String companyName) {
//        return session.createQuery("select u from User u " +
//                        "join u.company c " +
//                        "where c.name = :companyName", User.class)
//                .setParameter("companyName", companyName)
//                .list();

        HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

        JpaCriteriaQuery<User> criteria = criteriaBuilder.createQuery(User.class);
        JpaRoot<Company> company = criteria.from(Company.class);
        JpaMapJoin<Company, String, User> users = company.join(Company_.users);

        criteria.select(users)
                .where(criteriaBuilder.equal(company.get(Company_.name), companyName));

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает все выплаты, полученные сотрудниками компании с указанными именем,
     * упорядоченные по имени сотрудника, а затем по размеру выплаты
     */
    public List<Payment> findAllPaymentsByCompanyName(Session session, String companyName) {
//        return session.createQuery("select p from Payment p " +
//                "join p.user u " +
//                        "join u.company c " +
//                        "where c.name = :companyName " +
//                        "order by u.personalInfo.firstname, p.amount", Payment.class)
//                .setParameter("companyName", companyName)
//                .list();

        HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

        JpaCriteriaQuery<Payment> criteria = criteriaBuilder.createQuery(Payment.class);
        JpaRoot<Payment> payment = criteria.from(Payment.class);
        JpaJoin<Payment, User> user = payment.join(Payment_.receiver);
        JpaJoin<User, Company> company = user.join(User_.company);

        criteria.select(payment)
                .where(criteriaBuilder.equal(company.get(Company_.name), companyName))
                .orderBy(criteriaBuilder.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)),
                        criteriaBuilder.asc(payment.get(Payment_.amount)));

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает среднюю зарплату сотрудника с указанными именем и фамилией
     */
    public Double findAveragePaymentAmountByFirstAndLastNames(Session session, String firstName, String lastName) {
//        return session.createQuery("select avg(p.amount) from Payment p " +
//                        "join p.receiver u " +
//                        "where u.personalInfo.firstname = :firstname and u.personalInfo.lastname = :lastname ", Double.class)
//                .setParameter("firstname", firstName)
//                .setParameter("lastname", lastName)
//                .uniqueResult();

        HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

        JpaCriteriaQuery<Double> criteria = criteriaBuilder.createQuery(Double.class);
        JpaRoot<Payment> payment = criteria.from(Payment.class);
        JpaJoin<Payment, User> users = payment.join(Payment_.receiver);

        List<Predicate> predicates = new ArrayList<>();
        if(firstName != null) {
            predicates.add(criteriaBuilder.equal(users.get(User_.personalInfo).get(PersonalInfo_.firstname), firstName));
        }
        if(lastName != null) {
            predicates.add(criteriaBuilder.equal(users.get(User_.personalInfo).get(PersonalInfo_.lastname), lastName));
        }

        criteria.select(criteriaBuilder.avg(payment.get(Payment_.amount)))
                .where(predicates.toArray(Predicate[]::new));

        return session.createQuery(criteria)
                .uniqueResult();
    }

    /**
     * Возвращает для каждой компании: название, среднюю зарплату всех её сотрудников. Компании упорядочены по названию.
     */
    public List<CompanyDto> findCompanyNamesWithAvgUserPaymentsOrderedByCompanyName(Session session) {
//        return session.createQuery("select c.name, avg(p.amount) from Company c " +
//                        "join c.users u " +
//                        "join u.payments p " +
//                        "group by c.name", Object[].class)
//                .list();

        HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

        JpaCriteriaQuery<CompanyDto> criteria = criteriaBuilder.createQuery(CompanyDto.class);
        JpaRoot<Company> company = criteria.from(Company.class);
        JpaMapJoin<Company, String, User> user = company.join(Company_.users, JoinType.INNER);
        JpaListJoin<User, Payment> payment = user.join(User_.payments);

        criteria.select(criteriaBuilder.construct(CompanyDto.class,
                        company.get(Company_.name),
                        criteriaBuilder.avg(payment.get(Payment_.amount))))
                .groupBy(company.get(Company_.name))
                .orderBy(criteriaBuilder.asc(company.get(Company_.name)));

        return session.createQuery(criteria)
                .list();
    }

    /**
     * Возвращает список: сотрудник (объект User), средний размер выплат, но только для тех сотрудников, чей средний размер выплат
     * больше среднего размера выплат всех сотрудников
     * Упорядочить по имени сотрудника
     */
    public List<Tuple> isItPossible(Session session) {
//        return session.createQuery("select u, avg(p.amount) from User u " +
//                "join u.payments p " +
//                        "group by u " +
//                        "having avg(p.amount) > (select avg(p.amount) from Payment p) " +
//                        "order by u.personalInfo.firstname", Object[].class)
//                .list();

        HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

        JpaCriteriaQuery<Tuple> criteria = criteriaBuilder.createQuery(Tuple.class);
        JpaRoot<User> user = criteria.from(User.class);
        JpaListJoin<User, Payment> payment = user.join(User_.payments);

        JpaSubQuery<Double> subQuery = criteria.subquery(Double.class);
        JpaRoot<Payment> paymentSubQuery = subQuery.from(Payment.class);

        criteria.multiselect(user, criteriaBuilder.avg(payment.get(Payment_.amount)))
                .groupBy(user)
                .having(criteriaBuilder.gt(criteriaBuilder.avg(payment.get(Payment_.amount)),
                        subQuery.select(criteriaBuilder.avg(paymentSubQuery.get(Payment_.amount)))
                        ))
                .orderBy(criteriaBuilder.asc(user.get(User_.personalInfo).get(PersonalInfo_.firstname)));

        return session.createQuery(criteria)
                .list();
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}
