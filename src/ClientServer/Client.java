package ClientServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

	/**
	 * @param args
	 */
	private Socket clientSocket;
	private ObjectOutputStream outStream;
	private ObjectInputStream inStream;
	/**
	 * Oeffnet ein Socket und versucht eine Verbindung zum Server aufzubauen.
	 * @param serverName
	 * @param serverPort
	 * @return true, wenn Verbindung erstellt wurde
	 */
	public boolean connectToServer(String serverName, int serverPort){
		try {
			clientSocket = new Socket(serverName, serverPort);
			inStream = new ObjectInputStream(clientSocket.getInputStream());
			outStream = new ObjectOutputStream(clientSocket.getOutputStream());
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	public boolean sendRequest(Request request){
		try {
			if(clientSocket!=null){
				outStream.writeObject(request);
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

	public Response receiveResponse(){
        Response r;
		if(clientSocket!=null){
			try {
				
				try {
					r = (Response)inStream.readObject();
					
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
	 * Schliesst Verbindung zu Server.
	 */
	public void closeConnection(){
		try {
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}