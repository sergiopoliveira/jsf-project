package util;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.hibernate.Session;

public class PhaseListenerProj implements PhaseListener {

	private static final long serialVersionUID = 1L;

	@Override
	public void beforePhase(PhaseEvent phase) {

		if (phase.getPhaseId().equals(PhaseId.RESTORE_VIEW)) {

			System.out.println("Antes da fase: " + getPhaseId());
			Session session = HibernateUtil.getSessionfactory().openSession();
			session.beginTransaction();
			FacesContextUtil.setRequestSession(session);
		}
	}

	@Override
	public void afterPhase(PhaseEvent phase) {

		System.out.println("Depois da fase: " + getPhaseId());
		if (phase.getPhaseId().equals(PhaseId.RENDER_RESPONSE)) {

			Session session = FacesContextUtil.getRequestSession();

			try {
				session.getTransaction().commit();
			} catch (Exception e) {
				if (session.getTransaction().isActive()) {
					session.getTransaction().rollback();
				}
			} finally {
				session.close();
			}
		}

	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}
}
