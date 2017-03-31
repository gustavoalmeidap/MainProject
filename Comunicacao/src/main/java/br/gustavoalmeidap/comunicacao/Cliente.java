package br.gustavoalmeidap.comunicacao;

import br.gustavoalmeidap.arquivolog.Arquivo;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Date;

public class Cliente {
    private Socket socket;
    private InputStream inputStream = null;    
    private Thread thread;
    private boolean conectado = false;
    private String ipServidor = null;
    private Integer porta = null;
    private String nome = null;
   
    public Cliente(String ipServidor, int porta, String nome) {
        this.ipServidor = ipServidor;
        this.porta = porta;
        this.nome = nome;
    }
    
    public void conectar() throws IOException{
        try {            
            socket = new Socket(ipServidor, porta);
            socket.setKeepAlive(true);
            socket.setTcpNoDelay(true);
            socket.setSoTimeout(1000);
            conectado = true;
            
            iniciarThreadReceive();      
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Falha ao conectar no servidor!");
        }
    }
   
    private void iniciarThreadReceive(){
        this.thread = new Thread(new Runnable() {
            byte[] data;
            String mensagemRecebida;
            Arquivo arquivo = new Arquivo();
            @Override
            public void run() {
                try {
                    while(Cliente.this.conectado && !Cliente.this.thread.isInterrupted()){
                        if(socket!=null){                            
                            inputStream = socket.getInputStream();
                            if(inputStream!=null && socket.isConnected()){                                
                                int tamanho = inputStream.available();
                                if(tamanho>0){
                                    data = new byte[tamanho];
                                    inputStream.read(data);
                                    mensagemRecebida = new String(data);
                                    
                                    Date dataHora = new Date();
                                    
                                    arquivo.gravaRegistroLog(dataHora, nome, mensagemRecebida);
                                }
                            }else{
                                Cliente.this.desconectar();
                            }
                        }else{
                            Cliente.this.desconectar();
                        }
                    }
                    System.out.println("Finalizando recebimento...");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        thread.start();        
    }
    
    public void desconectar(){
        System.out.println("Entrou");
        try {
            if(thread!=null && conectado){                
                thread.interrupt();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                conectado = false;
                inputStream.close();
                socket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    } 
}
