import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

public class TypingWarsServer {
	
	public static void main (String[] args) {
		ServerSocket serversocket = null;
		try{
			serversocket = new ServerSocket(9845);
			System.out.println("Rodando Servidor na porta 9845");
		}catch(Exception e){
			System.out.println("Erro ao conectar à porta\n" + e);
			return;
		}
		
		for(int i = 1; i <= 2; i++){
			Socket clientsocket = null;
			try{
				clientsocket = serversocket.accept();
					//System.out.println(clientsocket);
			}catch(Exception e){
				System.out.println("Erro ao conectar cliente\n" + e);
				return;
			}
			System.out.println("Jogador " + i + " conectado");
			//System.out.println(clientsocket);
			new Servindo(clientsocket).start();
		}
		try{
			serversocket.close();
		}catch(Exception e){
			System.out.println("Erro ao fechar conexão\n" + e);
		}
	}
}

class Servindo extends Thread{
	Socket clientesocket;
	static PrintStream os[] = new PrintStream[2];
	static int cont = 0;
	
	Servindo(Socket clientSocket) {
		
		this.clientesocket = clientSocket;
  }
	
	public void run(){
		try{
			Scanner is = new Scanner(clientesocket.getInputStream());
			os[cont] = new PrintStream(clientesocket.getOutputStream());
			String inputLine, outputLine;
			cont++;
			boolean vez1 = true;
			int c = 0;
			do{
				if(vez1){
					os[cont-1].println(cont);
					vez1 = false;
				}
				inputLine = is.nextLine();
				for(int i = 0; i < cont; i++){
					os[i].println(inputLine);
					os[i].flush();
				}
			}while(!inputLine.equals("ACABOU!"));
			
			for(int i = 0; i < cont; i++)	os[i].close();
			is.close();
			clientesocket.close();
		}catch(Exception e){
			//e.printStackTrace();
			System.out.println("Conexão encerrada\n");
		}
	}
};

