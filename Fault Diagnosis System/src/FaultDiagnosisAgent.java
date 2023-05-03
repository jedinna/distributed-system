import jade.core.AID;
import jade.core.Agent;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import java.util.ArrayList;

public class FaultDiagnosisAgent extends Agent {

    private ArrayList<AID> monitoringAgents = new ArrayList<AID>();

    @Override
    protected void setup() {
        System.out.println("Fault diagnosis agent " + getAID().getName() + " is ready.");

        // Register the fault diagnosis service
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("fault-diagnosis");
        sd.setName("fault-diagnosis-service");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        // Find monitoring agents
        addBehaviour(new FindMonitoringAgents());
    }

    private class FindMonitoringAgents extends OneShotBehaviour {

        @Override
        public void action() {
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("monitoring");
            template.addServices(sd);
            try {
                DFAgentDescription[] result = DFService.search(myAgent, template);
                for (int i = 0; i < result.length; ++i) {
                    monitoringAgents.add(result[i].getName());
                }
                addBehaviour(new ReceiveFault());
            } catch (FIPAException e) {
                e.printStackTrace();
            }
        }
    }

    private class ReceiveFault extends CyclicBehaviour {

        @Override
        public void action() {
            ACLMessage msg = myAgent.receive();
            if (msg != null) {
                System.out.println("Fault received from " + msg.getSender().getName());
                // Process the fault and diagnose the problem
            } else {
                block();
            }
        }
    }
}
