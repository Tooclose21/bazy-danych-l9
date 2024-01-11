import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();

        // a)
        System.out.println("a)");
        String queryStringA = "SELECT 100.0 * COUNT(p) / ((SELECT COUNT(p2) FROM ProductsEntity p2)) FROM ProductsEntity p WHERE p.calcium + p.iron > 50";
        Query queryA = em.createQuery(queryStringA);
        Float resultA = (Float) queryA.getSingleResult();

        System.out.printf("Procent produktów spełniających wymóg: %.2f%%\n", resultA);

        // b)
        System.out.println("b)");
        String queryStringB = "SELECT AVG(p.calories) FROM ProductsEntity p WHERE p.itemName LIKE :arg";
        Query queryB = em.createQuery(queryStringB);
        queryB.setParameter("arg", "%bacon%");
        System.out.printf("Średnia liczba kalori dla produktów z bekonem to: %.2f\n", (Double) queryB.getSingleResult());

        // c)
        System.out.println("c)");
        String queryStringC = "SELECT c.catName, max(p.cholesterole) FROM ProductsEntity p JOIN p.category c GROUP BY c.catName";
        Query queryC = em.createQuery(queryStringC);

        List<Object[]> resultList = queryC.getResultList();
        for (Object[] result : resultList) {
            System.out.printf("Category: %s, calories: %d\n", result[0], (int)result[1]);
        }

        // d)
        System.out.println("d)");
        String queryStringD = "SELECT COUNT(p.itemName) FROM ProductsEntity p WHERE p.itemName LIKE :c OR p.itemName LIKE :m AND p.fiber = 0";
        Query queryD = em.createQuery(queryStringD);
        queryD.setParameter("c", "%coffee%");
        queryD.setParameter("m", "%mocha%");
        System.out.printf("Kawy bez błonnika: %s\n", queryD.getSingleResult());

        // e)
        System.out.println("e)");
        String queryStringE = "SELECT p.itemName, p.calories / 4184.0 FROM ProductsEntity p WHERE p.itemName LIKE :mc";
        Query queryE = em.createQuery(queryStringE);
        queryE.setParameter("mc", "%mcmuffin%");
        List<Object[]> resultListE = queryE.getResultList();

        for (Object[] result : resultListE) {
            System.out.printf("McMuffin: %s, kJ: %s\n", result[0], result[1]);
        }

        // f)
        System.out.println("f)");
        String queryStringF = "SELECT COUNT(DISTINCT(p.carbs)) FROM ProductsEntity p";
        Query queryF = em.createQuery(queryStringF);
        System.out.printf("Liczba różnych wartości kalorii: %d", (Long) queryF.getSingleResult());

    }
}
