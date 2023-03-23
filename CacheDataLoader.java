/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ladend.effect;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import javax.imageio.ImageIO;

/**
 *
 * @author Admin
 */
public class CacheDataLoader {      //cách lưu data trong bộ nhớ trong: Cache
    
    private static CacheDataLoader instance = null;
    
    
    private String framefile ="data/frame.txt";
    private String animationfile ="data/animation.txt";
    private String physmapfile ="data/phys_map.txt";
    private String backgroundmapfile ="data/background_map.txt";
    private String soundfile = "data/sounds.txt";

    
    private Hashtable<String, FrameImage> frameImages;          //Bảng băm - cấu trúc dữ liệu
    private Hashtable<String, Animation> animations;            //Lay phan tu theo key(String) thay vi index
    private Hashtable<String, AudioClip> sounds;

    
    private int [][] phys_map;
    private int [][] background_map;
    
    
    private CacheDataLoader(){}          //để tạo theo kiểu mẫu thiết kế Singleton thì phải loại bỏ Constructer-ko cho su dung-dat thanh private
                                                                    //ko the tao ra 1 lop co thuc the cua no = cau lenh "new" dc 
    
    public static CacheDataLoader getInstance(){    //muon lay dc instance thi buoc phai thong qua phuong thuc nay moi tao ra dc 1 doi tuong moi
                                                    //để tạo ra 1 instance duy nhất, Mẫu Singleton là một mẫu hạn chế số lượng đối tượng của một lớp
        if(instance == null)                        //tránh tạo ra các đối tượng thừa
            instance = new CacheDataLoader();
        return instance;
    }

    public int[][] getPhysicalMap() {
        return instance.phys_map;
    }

    public int[][] getBackgroundmap() {
        return instance.background_map;
    }
    
    public AudioClip getSound(String name){
        return instance.sounds.get(name);
    }
    
    public FrameImage getFrameImage(String name){
        FrameImage frameImage = new FrameImage(instance.frameImages.get(name));
        return frameImage;
    }
    public Animation getAnimation(String name){
        Animation animation = new Animation(instance.animations.get(name));
        return animation;
    }
    
    public void LoadData() throws IOException{
        LoadFrame();
        LoadAnimation();
        LoadPhysMap();
        LoadBackgroundMap();
        LoadSounds();

    }
    
    public void LoadSounds() throws IOException{
        sounds = new Hashtable<String, AudioClip>();
        
        FileReader fr = new FileReader(soundfile);
        BufferedReader br = new BufferedReader(fr);
        
        String line = null;
        
        if(br.readLine()==null) { // no line = "" or something like that
            System.out.println("No data");
            throw new IOException();
        }
        else {
            
            fr = new FileReader(soundfile);
            br = new BufferedReader(fr);
            
            while((line = br.readLine()).equals(""));
            
            int n = Integer.parseInt(line);
            
            for(int i = 0;i < n; i ++){
                
                AudioClip audioClip = null;
                while((line = br.readLine()).equals(""));

                String[] str = line.split(" ");
                String name = str[0];
                
                String path = str[1];

                try {
                   audioClip =  Applet.newAudioClip(new URL("file","",str[1]));

                } catch (MalformedURLException ex) {}
                
                instance.sounds.put(name, audioClip);
            }
            
        }
        
        br.close();
        
    }
    
    public void LoadPhysMap()throws IOException{
        
        FileReader fr = new FileReader(physmapfile);            //luồng để lấy dữ liệu về
        BufferedReader br = new BufferedReader(fr);             //đọc dữ liệu từ luồng
        
        String line = null;
        
        line = br.readLine();
        int numberOfRows = Integer.parseInt(line);
        line = br.readLine();
        int numberOfColumns = Integer.parseInt(line);
        
        instance.phys_map = new int [numberOfRows][numberOfColumns];
        
        for(int i=0;i<numberOfRows;i++){
            line = br.readLine();
            String [] str = line.split(" ");
            for(int j=0;j<numberOfColumns;j++)
                instance.phys_map [i][j] = Integer.parseInt(str[j]);
        }
        
        for(int i=0;i<numberOfRows;i++){
            for(int j=0;j<numberOfColumns;j++)
                System.out.print(" "+instance.phys_map[i][j]);
            System.out.println();
        }
        br.close();
    }
    
        public void LoadBackgroundMap() throws IOException{
        
        FileReader fr = new FileReader(backgroundmapfile);
        BufferedReader br = new BufferedReader(fr);
        
        String line = null;
        
        line = br.readLine();
        int numberOfRows = Integer.parseInt(line);
        line = br.readLine();
        int numberOfColumns = Integer.parseInt(line);
            
        
        instance.background_map = new int[numberOfRows][numberOfColumns];
        
        for(int i = 0;i < numberOfRows;i++){
            line = br.readLine();
            String [] str = line.split(" |  ");
            for(int j = 0;j<numberOfColumns;j++)
                instance.background_map[i][j] = Integer.parseInt(str[j]);
        }
        
        for(int i = 0;i < numberOfRows;i++){
            
            for(int j = 0;j<numberOfColumns;j++)
                System.out.print(" "+instance.background_map[i][j]);
            
            System.out.println();
        }
        
        br.close();
        
    }
    
    public void LoadFrame() throws IOException{                 //load dlieu Frame vao hashtable
        
        frameImages = new Hashtable<String,FrameImage>();
        
        FileReader fr = new FileReader(framefile);
        BufferedReader br = new BufferedReader(fr);         //con tro của buffered đọc nextLine, nên bắt đầu từ dòng 2
        
        String line = null;         //lưu từng line để đọc data trong file txt
        
        if(br.readLine()==null){                //ktra file
            System.out.println("No data");
            throw new IOException();
        }
        else {
            fr = new FileReader(framefile);
            br = new BufferedReader(fr);            //dua con tro doc file ve vi tri dau
            
            while ((line = br.readLine()).equals(""));      //ktra chuoi rong
            
            int n = Integer.parseInt(line);          //ép sang kiểu int- xac dinh số lần lặp for là số dòng dữ liệu đã lưu trong txt
            
            for(int i=0;i<n;i++){
                
                FrameImage frame = new FrameImage();
                while((line = br.readLine()).equals(""));
                frame.setName(line);
                
                while((line = br.readLine()).equals(""));
                String[] str = line.split(" ");
                String path = str[1];
                
                while((line = br.readLine()).equals(""));
                str = line.split(" ");
                int x = Integer.parseInt(str[1]);
                
                while((line = br.readLine()).equals(""));
                str = line.split(" ");
                int y = Integer.parseInt(str[1]);
                
                while((line = br.readLine()).equals(""));
                str = line.split(" ");
                int w = Integer.parseInt(str[1]);
                
                while((line = br.readLine()).equals(""));
                str = line.split(" ");
                int h = Integer.parseInt(str[1]);
                
                BufferedImage imageData = ImageIO.read(new File(path));     //đọc hình to
                BufferedImage image = imageData.getSubimage(x,y,w,h);       //lấy ra hình nhỏ
                frame.setImage(image);
                
                instance.frameImages.put(frame.getName(),frame); //put vào lưu ở dạng (key, value)
            }
        }
        br.close();
        
    }
    
    public void LoadAnimation() throws IOException{
        animations = new Hashtable<String, Animation>();
        
        FileReader fr = new FileReader(animationfile);
        BufferedReader br = new BufferedReader(fr);
        
        String line = null;
        
        if(br.readLine()==null){
            System.out.println("No data");
            throw new IOException();
        }
        else{
            fr = new FileReader(animationfile);
            br = new BufferedReader(fr);
            
            while((line = br.readLine()).equals(""));
            int n = Integer.parseInt(line);
            
            for(int i =0;i<n;i++){
                
                Animation animation = new Animation();
                
                while((line = br.readLine()).equals(""));
                animation.setName(line);
                
                while((line = br.readLine()).equals(""));
                String[] str =line.split(" ");
                
                for(int j=0;j<str.length;j+=2){
                    animation.add(getFrameImage(str[j]), Double.parseDouble(str[j+1]));
                }
                
                instance.animations.put(animation.getName(), animation);
                
            }
        }
        br.close();
    }

}













