/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllerDao;

import clase.PspUsers;
import clase.exceptions.NonexistentEntityException;
import clase.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author eetxa
 */
public class PspUsersJpaController implements Serializable {

    public PspUsersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PspUsers pspUsers) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(pspUsers);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPspUsers(pspUsers.getUsername()) != null) {
                throw new PreexistingEntityException("PspUsers " + pspUsers + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PspUsers pspUsers) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            pspUsers = em.merge(pspUsers);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = pspUsers.getUsername();
                if (findPspUsers(id) == null) {
                    throw new NonexistentEntityException("The pspUsers with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PspUsers pspUsers;
            try {
                pspUsers = em.getReference(PspUsers.class, id);
                pspUsers.getUsername();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pspUsers with id " + id + " no longer exists.", enfe);
            }
            em.remove(pspUsers);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PspUsers> findPspUsersEntities() {
        return findPspUsersEntities(true, -1, -1);
    }

    public List<PspUsers> findPspUsersEntities(int maxResults, int firstResult) {
        return findPspUsersEntities(false, maxResults, firstResult);
    }

    private List<PspUsers> findPspUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PspUsers.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public PspUsers findPspUsers(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PspUsers.class, id);
        } finally {
            em.close();
        }
    }

    public PspUsers findPspUsertoken(BigInteger validationToken) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                    "SELECT u FROM PspUsers u WHERE u.validationToken = :validationToken", PspUsers.class)
                    .setParameter("validationToken", validationToken)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Retorna null si no encuentra ningún usuario con ese token
        } finally {
            em.close();
        }
    }

    public int getPspUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PspUsers> rt = cq.from(PspUsers.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
