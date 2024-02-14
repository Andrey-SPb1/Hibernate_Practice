package com.andrey;

import com.andrey.entity.Company;
import com.andrey.entity.User;
import com.andrey.entity.UserChat;
import com.andrey.util.HibernateUtil;
import com.andrey.util.TestDataImporter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;

import java.util.List;
import java.util.Map;

@Slf4j
public class HibernateRunner {

    public static void main(String[] args) {
        try(SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
            Session session = sessionFactory.openSession()) {
            session.beginTransaction();
//            session.enableFetchProfile("withCompanyAndPayments");

            RootGraph<User> userRootGraph = session.createEntityGraph(User.class);
            userRootGraph.addAttributeNodes("company", "userChats");
            SubGraph<UserChat> userChatSubGraph = userRootGraph.addSubgraph("userChats", UserChat.class);
            userChatSubGraph.addAttributeNodes("chat");

            Map<String, Object> properties = Map.of(
                    GraphSemantic.LOAD.getJpaHintName(),
//                    session.getEntityGraph("withCompanyAndChat")
                    userRootGraph
            );

            User user = session.find(User.class, 1L, properties);
            System.out.println(user.getCompany().getName());
            System.out.println(user.getUserChats().size());
//            System.out.println(user.getPayments().size());

            List<User> users = session.createQuery("select u from User u ", User.class)
                    .setHint(GraphSemantic.LOAD.getJpaHintName(),
//                            session.getEntityGraph("withCompanyAndChat")
                            userRootGraph
                    )
                    .list();
            users.forEach(it -> System.out.println(it.getUserChats().size()));
            users.forEach(it -> System.out.println(it.getCompany().getName()));

            session.getTransaction().commit();
        }
    }
}
