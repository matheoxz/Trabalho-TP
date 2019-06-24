import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.Color;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import javax.imageio.*;
import java.io.*;
import java.io.File; 
import java.io.IOException;
import java.util.ArrayList; 
import java.util.Random; 
import java.util.Scanner; 
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;

class PalavraAleatoria{
	ArrayList<String> Palavras = new ArrayList<String>();
	PalavraAleatoria(){
		try{
			File arq = new File("palavras.txt");
				
			if(arq.exists()){
				FileReader reader = new FileReader(arq);
				BufferedReader breader = new BufferedReader(reader);
				while(breader.ready()){
					String line = breader.readLine();
					Palavras.add(line);
				}
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "O arquivo de palavras não pode ser carregada!\n" + e, "Erro", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}
	}
	public String palavraAleatoria(){
		Random r = new Random();
		return Palavras.get(r.nextInt(Palavras.size()));
	}
}

class Conexao implements Runnable {
			static Desenho des = new Desenho();
			static PrintStream os = null;
			boolean a= true;
			Tabuleiro tab = new Tabuleiro();
			static int jog, estado, estado2;
			static int frente[] = new int[2];
			String[] posicao;
			
			public void run () {
				Socket socket= null;
				Scanner is = null;
				
				try {
					socket = new Socket("localhost", 9845);
					os = new PrintStream(socket.getOutputStream(), true);
					is = new Scanner(socket.getInputStream());
				} catch (UnknownHostException e) {
					System.err.println("Não achou servidor");
				} catch (IOException e) {
					System.err.println("Incapaz de pegar I/O na conexão");
				}
			
				try {
					String inputLine;	
					do{
						System.out.println(inputLine = is.nextLine());
						
						if (a) {
							
							posicao = inputLine.split(Pattern.quote(" "));
							if(posicao[0].equals("1")){
								a=false;
								tab.jogador1[0]=0;
								tab.jogador1[1]=4;
								tab.jogador2[0]=4;
								tab.jogador2[1]=0;
								jog = 1;
								estado = 1;
								estado2 = 3;
								des.jog1 = des.p1_dir;
								des.jog2 = des.p2_esq;
								
							}
							else if(posicao[0].equals("2")){
								a=false;
								tab.jogador1[0]=4;
								tab.jogador1[1]=0;
								tab.jogador2[0]=0;
								tab.jogador2[1]=4;
								jog = 2;
								estado = 3;
								estado2 = 1;
								des.jog1 = des.p2_esq;
								des.jog2 = des.p1_dir;
								
							}
							
							if(jog == 1) 	  JOptionPane.showMessageDialog(null, "VOCÊ É O JOGADOR 1 \n OTTO VON BISMARCK");
							else if(jog == 2) JOptionPane.showMessageDialog(null, "VOCÊ É O JOGADOR 2 \n NAPOLEÃO BONAPARTE");
						}
						else if(inputLine.equals("Acabou1")){
							JOptionPane.showMessageDialog(null, "JOGADOR 1 GANHOU");	
							a = true;
						}	
						else if(inputLine.equals("Acabou2")){
							JOptionPane.showMessageDialog(null, "JOGADOR 2 GANHOU");
							a = true;
						}	
						else{
							posicao = inputLine.split(Pattern.quote(" "));
							if(Integer.parseInt(posicao[0]) != jog) {
								if (tab.jogador2[0] < Integer.parseInt(posicao[2])){
									if (jog == 2)
										des.jog2 = des.p1_dir;
									if (jog == 1)
										des.jog2 = des.p2_dir;
										estado2 = 1;
								}
								if (tab.jogador2[0] > Integer.parseInt(posicao[2])){
									if (jog == 2)
										des.jog2=des.p1_esq;
									if (jog == 1)
										des.jog2=des.p2_esq;
										estado2 = 3;
								}
								if (tab.jogador2[1] < Integer.parseInt(posicao[1])){
									if (jog == 2)
										des.jog2=des.p1_baixo;
									if (jog == 1)
										des.jog2=des.p2_baixo;
										estado2 = 4;
								}
								if (tab.jogador2[1] > Integer.parseInt(posicao[1])){
									if (jog == 2)
										des.jog2=des.p1_cima;
									if (jog == 1)
										des.jog2=des.p2_cima;
										estado2 = 2;
								}
								tab.jogador2[0] = Integer.parseInt(posicao[2]);
								tab.jogador2[1] = Integer.parseInt(posicao[1]);
							}
						}
							
						des.validate();
						des.repaint();
					}while (!inputLine.equals("Finaliza"));
					os.close();
					is.close();
					socket.close();
				} catch (UnknownHostException e) {
					System.err.println("Trying to connect to unknown host: " + e);
				} catch (IOException e) {
					System.err.println("IOException:  " + e);
				}
				
		}
}

class Desenho extends JPanel {
	Image jog1, jog2, fundo;
	Image p1_dir, p1_esq, p1_cima, p1_baixo;
	Image p2_dir, p2_esq, p2_cima, p2_baixo;
	Tabuleiro tab = new Tabuleiro();
	
	
	Desenho(){
		try{
				p1_dir = ImageIO.read(new File("p1_dir.png"));
				p1_esq = ImageIO.read(new File("p1_esq.png"));
				p1_cima = ImageIO.read(new File("p1_cima.png"));
				p1_baixo = ImageIO.read(new File("p1_baixo.png"));
				p2_dir = ImageIO.read(new File("p2_dir.png"));
				p2_esq = ImageIO.read(new File("p2_esq.png"));
				p2_cima = ImageIO.read(new File("p2_cima.png"));
				p2_baixo = ImageIO.read(new File("p2_baixo.png"));
				fundo = ImageIO.read(new File("fundo.jpg"));
			}catch(IOException e){
				JOptionPane.showMessageDialog(null, "Imagem não pode ser carregada!\n" + e, "Erro", JOptionPane.ERROR_MESSAGE);
			}
	}
	
	public void paintComponent(Graphics g) {
		  super.paintComponent(g);
		  g.drawImage(fundo, 0,0,1000,1000, this);
		  for (int i=200, j=180; i<1000; i+=200, j+=180){
			g.drawLine(i,0,i,900);
			g.drawLine(0,j,1000,j);
			//System.out.println("repaint");
		}
		for(int i = 0, y = 160; i < 5; i++, y+=180)
			for(int j = 0, x = 20; j < 5; j++, x+=200)
				g.drawString(tab.tabumat[i][j], x, y);
		
		  g.drawImage(jog1,tab.jogador1[0]*200+20, tab.jogador1[1]*180+10, 160 , 130, this);
		  g.drawImage(jog2,tab.jogador2[0]*200+20, tab.jogador2[1]*180+10, 160 , 130, this);
		  Toolkit.getDefaultToolkit().sync();
    }
}

class Tabuleiro{
	static public String tabumat[][] = new String[5][5];
	PalavraAleatoria pa = new PalavraAleatoria();
	static int jogador1[] = new int [2];
	static int jogador2[] = new int [2];
	void geraTabuleiro () {
		String palteste = new String();
		boolean deu=false;
	 
		for(int i = 0; i < 5; i++){
				for(int j = 0; j < 5; j++){
					
					while(!deu){
						palteste = pa.palavraAleatoria();
						
						for (int k=0; k<=i; k++){
							for (int l=0;l<=j; l++) {
								
								if (palteste.equals(tabumat[k][l])){
									deu=false;
									break;
								}
								deu=true;
							}
							if (!deu)
							break;
						}
						
					}
					
					deu=false;
					tabumat[i][j]=palteste;
					//System.out.print(tabumat[i][j] + " ");
				}
				//System.out.println();
			}
	}
}

class Tela extends JFrame{
	Tabuleiro tab = new Tabuleiro();
	//Desenho des = new Desenho();
	Conexao con = new Conexao();
	JTextField direcao = new JTextField(30);
	
	
	Tela(){
		super("TypingWars");
		setPreferredSize(new Dimension(1000,1000));
		setLayout(new BorderLayout());
		
		
		direcao.setPreferredSize(new Dimension(20, 100));
		direcao.setFont(new java.awt.Font("Ubuntu", 0, 30));
		direcao.addActionListener(new AbstractAction(){
			@Override
			public void actionPerformed(ActionEvent e){
				String palavra = direcao.getText();
				
				if(tab.jogador1[1]+1 < 5 && palavra.equals(tab.tabumat[tab.jogador1[1]+1][tab.jogador1[0]])){
					if (tab.jogador1[1]+1 == tab.jogador2[1] && tab.jogador1[0] == tab.jogador2[0]){
						if(con.estado2 != 2)
							con.os.println("Acabou" + con.jog);
					}
					else {
						tab.jogador1[1] += 1;
						if (con.jog == 1){
							con.des.jog1=con.des.p1_baixo;
							con.estado = 4;
						}
						if (con.jog == 2){
							con.des.jog1=con.des.p2_baixo;
							con.estado = 4;
						}	
					}
				}
				
				if(tab.jogador1[1]-1 >= 0 && palavra.equals(tab.tabumat[tab.jogador1[1]-1][tab.jogador1[0]])){
					if (tab.jogador1[1]-1 == tab.jogador2[1] && tab.jogador1[0] == tab.jogador2[0]){
						if(con.estado2 != 4)
							con.os.println("Acabou" + con.jog);
					}
					else {
						tab.jogador1[1] -= 1;
						if (con.jog == 1){
							con.des.jog1=con.des.p1_cima;
							con.estado = 2;
							}
						if (con.jog == 2){
							con.des.jog1=con.des.p2_cima;
							con.estado = 2;
						}	
					}
				}
					
				if(tab.jogador1[0]+1 < 5 && palavra.equals(tab.tabumat[tab.jogador1[1]][tab.jogador1[0]+1])){
					if (tab.jogador1[1] == tab.jogador2[1] && tab.jogador1[0]+1 == tab.jogador2[0]){
						if(con.estado2 != 3)
							con.os.println("Acabou" + con.jog);
					}
					else {
						tab.jogador1[0] += 1;
						if (con.jog == 1){
							con.des.jog1=con.des.p1_dir;
							con.estado = 1;
						}
						if (con.jog == 2){
							con.des.jog1=con.des.p2_dir;
							con.estado = 1;
							
						}
					}
				}
				if(tab.jogador1[0]-1 >= 0 && palavra.equals(tab.tabumat[tab.jogador1[1]][tab.jogador1[0]-1])){
					if (tab.jogador1[1] == tab.jogador2[1] && tab.jogador1[0]-1 == tab.jogador2[0]){
						if(con.estado2 != 1)
							con.os.println("Acabou" + con.jog);
					}
					else {
						tab.jogador1[0] -= 1;
						if (con.jog == 1){
							con.des.jog1=con.des.p1_esq;
							con.estado = 3;
						}
						if (con.jog == 2){
							con.des.jog1=con.des.p2_esq;
							con.estado = 3;
						}
					}
				}
				
				con.os.println(con.jog + " " + tab.jogador1[1] + " " + tab.jogador1[0] + " " + con.estado);
				direcao.setText("");
			}
		});
		tab.geraTabuleiro();
		
		add(direcao, BorderLayout.SOUTH);
		add(Conexao.des, BorderLayout.CENTER);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
}

public class TypingWars{
	 static public void main(String[] args) {
		new Tela();
		Conexao con = new Conexao();
		con.run();
	}
}
