/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gustavoalmeidap.arquivolog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gustavo
 */
public class Arquivo {    
    private static final String DIRETORIO_LOG = "logs";
    private static final String NOME_ARQUIVO_LOG = "log.log";
    
    public boolean gravaRegistroLog(Date dataHora, String nome, String mensagem) {
        boolean retorno;
        String linhaLog;
        
        /* Pega todos campos formatados para a data */
        SimpleDateFormat ano = new SimpleDateFormat("yyyy");
        SimpleDateFormat mes = new SimpleDateFormat("MM");
        SimpleDateFormat dia = new SimpleDateFormat("dd");
        SimpleDateFormat hora = new SimpleDateFormat("HH");
        SimpleDateFormat minuto = new SimpleDateFormat("mm");
        SimpleDateFormat segundo = new SimpleDateFormat("ss");
        
        /* Monta a linha a ser gravada no arquivo de log */
        linhaLog = dia.format(dataHora.getTime()) + "/";
        linhaLog += mes.format(dataHora.getTime()) + "/";
        linhaLog += ano.format(dataHora.getTime()) + " ";
        linhaLog += hora.format(dataHora.getTime()) + ":";
        linhaLog += minuto.format(dataHora.getTime()) + ":";
        linhaLog += segundo.format(dataHora.getTime());
        linhaLog += " - ";
        linhaLog += nome;
        linhaLog += " - ";
        linhaLog += mensagem;
        linhaLog += "\r\n";
        
        /* Tenta gravar a linha do arquivo padrao de logs */
        try {
            File dir = new File(DIRETORIO_LOG);
            if(!dir.exists()){
                dir.mkdir();
            }
            FileOutputStream file = new FileOutputStream(DIRETORIO_LOG + "/" + NOME_ARQUIVO_LOG, true);
            try (PrintStream out = new PrintStream(file)) {
                out.print(linhaLog);
            }
            retorno = true;
        } catch (FileNotFoundException e) {
            Logger.getLogger(Arquivo.class.getName()).log(Level.SEVERE, null, e);
            retorno = false;
        } 
        return retorno;
    }
}