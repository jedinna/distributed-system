import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;



public class MonitoringAgent extends Agent {

    public static void main(String[] args) {



    }

    private Timer timer = new Timer();

    @Override
    protected void setup() {
        System.out.println("Monitoring agent " + getAID().getName() + " is ready.");

        // Register the monitoring service
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("monitoring");
        sd.setName("monitoring-service");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

    }
}
