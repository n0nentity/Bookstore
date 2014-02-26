package ClientServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	/**
	 * @param args
	 */
	private ServerSocket serverSocket;
	private Socket connection; 
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
		/**
		 * Oeffnet Socket und wartet auf eingehende Verbindung.
		 * @param serverPort
		 * @param maxQueueLength
		 * @return true, wenn Verbindung erstellt.
		 */
		public boolean waitForClient(int serverPort, int maxQueueLength){
			try {
				serverSocket = new ServerSocket(serverPort, maxQueueLength);
				connection=serverSocket.accept();
				outStream = new ObjectOutputStream(connection.getOutputStream());
				inStream = new ObjectInputStream(connection.getInputStream());
				return true;
			} catch (Exception e) {
				// TODO: handle exception
				return false;
			}
		}

		public boolean sendResponse(Response r){
			try {
				if(connection!=null){
					
					outStream.writeObject(r);
					outStream.flush();
					
					return true;
				}
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();			
				return false;
			}
		}

		public Request receiveRequest(){
            Request r;
			if(connection!=null){
				try {
					
					try {
						r = (Request)inStream.readObject();
						return r;
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						return null;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
				
			}
			return null;
		}

		/**
		 * Schliesst die Verbindung.
		 */
		public void closeConnection(){
			try {
				serverSocket.close();
				connection.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}