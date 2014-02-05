package mqbrowser;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Date;
import javax.jms.Connection;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.QueueBrowser;
import javax.jms.Message;
import javax.jms.TextMessage;
import com.ibm.mq.jms.JMSC;
import com.ibm.mq.jms.MQConnectionFactory;
import com.ibm.mq.jms.MQQueue;
import com.ibm.msg.client.jms.internal.JmsStreamMessageImpl;
import com.ibm.jms.JMSMessage;
import com.ibm.msg.client.jms.JmsExceptionDetail;
import com.ibm.mq.MQException;
import com.ibm.mq.jmqi.JmqiException;
import javax.jms.JMSException;


public class MQBrowser {

	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		if(args.length <= 6) {
			System.out.println("ERROR: missing arguments, example arguments: scrbmqdefrm601.crb.apmoller.net 1416 KEWILL.CLIENT FAXAQM KEWILL anything <list of queues>");
			System.exit(255);
		}
		String conn_hostname = args[0];
		int conn_port = Integer.parseInt(args[1]);
		String conn_channel = args[2];
		String conn_queuemanager = args[3];
		String conn_username = args[4];
		String conn_password = args[5];
		ArrayList<String> conn_queues = new ArrayList<String>();
		for(int i = 7; i < args.length; i++) {
			conn_queues.add(args[i]);
		}

		MQConnectionFactory connectionFactory = new MQConnectionFactory();
		try {
			connectionFactory.setHostName(conn_hostname);
			connectionFactory.setPort(conn_port);
			if(!conn_channel.equals("none")) {
				connectionFactory.setChannel(conn_channel);
			}
			connectionFactory.setQueueManager(conn_queuemanager);
			connectionFactory.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);

		} catch (Exception ex) {
			System.out.println("NOT OK");
			System.out.println("Connection Factory failed");
			ex.printStackTrace();
			System.exit(255);
		}

		Connection connection;
		try {
			if(conn_username.equals("none") && conn_password.equals("none")) {
				connection = connectionFactory.createConnection();
			} else {
				connection = connectionFactory.createConnection(conn_username, conn_password);
			}
			
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			for (String queueName : conn_queues) {
				Queue queue = session.createQueue(queueName);
				((MQQueue) queue).getQueueName(); // TODO: Why do this?
				QueueBrowser queueBrowser = session.createBrowser(queue);
				Enumeration queueEnumerator = queueBrowser.getEnumeration();
				int count = 0; 
				while (queueEnumerator.hasMoreElements())
			      {
			            Message msg = (Message)queueEnumerator.nextElement();
			        	TextMessage message = (TextMessage) msg;
			        	System.out.println(message.getJMSMessageID());
			        	System.out.println(XmlUtils.formatXml(message.getText()));
			      }
			}
			
		} catch (JMSException ex) {
			System.out.println("NOT OK");
			System.out.println("Connection failed2");
			ex.printStackTrace();
			System.exit(255);
		}
		
		System.exit(0);
	}
}