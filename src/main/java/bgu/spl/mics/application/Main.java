package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.passiveObjects.JsonReader;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static bgu.spl.mics.application.passiveObjects.JsonReader.getInputFromJson;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */

public class Main {

    public static CountDownLatch countDownLatch= new CountDownLatch(4);
    public static void main(String[] args) throws InterruptedException, IOException {

       if (args.length!=2){
            System.out.println("Invalid argument");
            return;
        }
        String filePath = args[0];

        Input input = JsonReader.getInputFromJson(filePath); //"input.json"

        //Input input = JsonReader.getInputFromJson("input.json");
        Ewoks ewoks = Ewoks.getInstance();
        ewoks.load(input.getEwoks());

        //synchronization aid which allows threads to wait until the mandatory operations are performed by other threads
        //requires 4 threads to be completed before execution of main thread


        C3POMicroservice c3po = new C3POMicroservice();
        Thread t1 = new Thread(c3po);
        t1.start();
        HanSoloMicroservice hsnSolo = new HanSoloMicroservice();
        Thread t2 = new Thread(hsnSolo);
        t2.start();
        R2D2Microservice r2d2 = new R2D2Microservice(input.getR2D2());
        Thread t3 = new Thread(r2d2);
        t3.start();
        LandoMicroservice lando = new LandoMicroservice(input.getLando());
        Thread t4 = new Thread(lando);
        t4.start();

        countDownLatch.await(); //main thread now waits until other threads complete action

        LeiaMicroservice leia = new LeiaMicroservice(input.getAttacks());
        Thread t5 = new Thread(leia);
        t5.start();

        //todo check if need all this join or should delete
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();



        ////this is all for DEBUG!!! todo delete

        Diary diary = Diary.getInstance();
        System.out.println("Total Attacks: " +diary.getTotalAttacks());
        System.out.println("HanSolo Finish: " +diary.getHanSoloFinish());
        System.out.println("C3PO Finish: " +diary.getC3POFinish());
        System.out.println("R2D2 Deactivate: " +diary.getR2D2Deactivate());
        System.out.println("Leia Terminate: " +diary.getLeiaTerminate());
        System.out.println("HanSolo Terminate: " +diary.getHanSoloTerminate());
        System.out.println("C3PO Terminate: " +diary.getC3POTerminate());
        System.out.println("R2D2 Terminate: " +diary.getR2D2Terminate());
        System.out.println("Lando Terminate: " +diary.getLandoTerminate());



        Gson gson = new GsonBuilder().create();
        Diary myDiary = Diary.getInstance();
        try{
            //write to output file
            FileWriter fileWriter = new FileWriter(args[1]);//"Output.json"
            //FileWriter fileWriter = new FileWriter("Output.json");
            gson.toJson(myDiary, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch(Exception e){
            e.printStackTrace();
        }


    }

}
