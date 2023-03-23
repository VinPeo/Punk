/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ladend.effect;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author Admin
 */
public class Animation {
    
    private String name;
    
    private boolean isRepeated;     //lap lai hoat anh
    
    private ArrayList <FrameImage> frameImages;
    private int currentFrame;
    
    private ArrayList <Boolean> ignoreFrames;         //mang chua kieu T,F
    
    private ArrayList <Double> delayFrames;         //thoi gian delay giua cac frame
    
    private long beginTime;
    
    private boolean drawRectFrame;          //tao khung hitbox de de quan sat
    
    public Animation(){
        delayFrames = new ArrayList<Double>();
        beginTime = 0;
        currentFrame = 0;
        
        ignoreFrames = new ArrayList<Boolean>();
        
        frameImages = new ArrayList<FrameImage>();
        
        drawRectFrame = false;      //can thi moi set true
        
        isRepeated = true;          //khi ko can moi set false
    }
    
    public Animation(Animation animation){
        
        beginTime = animation.beginTime;
        currentFrame = animation.currentFrame;
        drawRectFrame = animation.drawRectFrame;
        isRepeated = animation.isRepeated;
        
        delayFrames = new ArrayList<Double>();
        for(Double d : animation.delayFrames){
            delayFrames.add(d);
        }
        
        ignoreFrames = new ArrayList<Boolean>();
        for(boolean b : animation.ignoreFrames){
            ignoreFrames.add(b);
        }
        
        frameImages = new ArrayList<FrameImage>();
        for(FrameImage f : animation.frameImages){
            frameImages.add(new FrameImage(f));
        }
    }
    
    
    //khoi tao
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsRepeated() {
        return isRepeated;
    }

    public void setIsRepeated(boolean isRepeated) {
        this.isRepeated = isRepeated;
    }

    public ArrayList<FrameImage> getFrameImages() {
        return frameImages;
    }

    public void setFrameImages(ArrayList<FrameImage> frameImages) {
        this.frameImages = frameImages;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        if(currentFrame >= 0 && currentFrame < frameImages.size())  //kt dk curr co hop le ko
            this.currentFrame = currentFrame;
        else this.currentFrame =0;
    }

    public ArrayList<Boolean> getIgnoreFrames() {
        return ignoreFrames;
    }

    public void setIgnoreFrames(ArrayList<Boolean> ignoreFrames) {
        this.ignoreFrames = ignoreFrames;
    }

    public ArrayList<Double> getDelayFrames() {
        return delayFrames;
    }

    public void setDelayFrames(ArrayList<Double> delayFrames) {
        this.delayFrames = delayFrames;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public boolean getDrawRectFrame() {
        return drawRectFrame;
    }

    public void setDrawRectFrame(boolean drawRectFrame) {
        this.drawRectFrame = drawRectFrame;
    }
    //-\\
    //phuong thuc bo sung
    
    public boolean isIgnoreFrame(int id){       //kiem tra xem frame do co phai ignore ko thi bo qua, nhay sang frame tiep theo
        return ignoreFrames.get(id);
    }
    //21:30-2.2
    public void setIgnoreFrame(int id){         //set frame nao muon ignore
        if(id >=0 && id <ignoreFrames.size())       //kt tham so ko hop le hoac lon hon size
            ignoreFrames.set(id,true);
    }
    
    public void unIgnoreFrame(int id){
        if(id >=0 && id <ignoreFrames.size())       //kt tham so ko hop le hoac lon hon size
            ignoreFrames.set(id,false);
    }
    
    public void reset(){
        currentFrame = 0;
        beginTime = 0;
        for(int i=0;i<ignoreFrames.size();i++)  //set lai toan bo mang thanh false - khong con frame nao bi ignore
            ignoreFrames.set(i,false);
    }
    
    public void add(FrameImage frameImage, double timeToNextFrame){     //add them frame de obj Image quan ly
        ignoreFrames.add(false);
        frameImages.add(frameImage);
        delayFrames.add(new Double(timeToNextFrame));
    }
    
    public BufferedImage getCurrentImage(){                 //tra ve cai image đúng, dang luu giu tai thoi diem do
        return frameImages.get(currentFrame).getImage();
    }
    
    public void Update(long currentTime){         //currentTime - tham so time cua he thong truyen vao, thuc hien update
        if(beginTime==0) beginTime = currentTime;     //beginTime ==0 : chua dc set => set = currentTime: tgian hien tai
        else{
            if(currentTime - beginTime > delayFrames.get(currentFrame)){      //kt nếu curr - begin > delay của ảnh hiện tại đã tính đc trc
                nextFrame();                                                //thì lập tức nhảy sang frame ảnh sau
                beginTime = currentTime;                                      //set lại time để sang ảnh tiếp theo
            }
        }
    }
    public void nextFrame(){
        if(currentFrame >= frameImages.size()-1){   //kt curr da di den cuoi pt cua mang frameI chua
            if(isRepeated) currentFrame = 0;        //nếu đã đến phần tử cuối thì lặp lại hành động đấy trong mảng FrameI
        }
        else currentFrame++;
        if(ignoreFrames.get(currentFrame))  nextFrame();        //neu frame bị đánh dấu là ignore thì skip qua mảng frameI đó
    }                                                           //để sang mảng frameI tiếp theo  

    public boolean isLastFrame(){
        if(currentFrame == frameImages.size() - 1)      //ham ktra xem day co phai la frame cuoi ko, thi chuyen qua trang thai khac
            return true;                                //tranh lap lai hanh dong day
        else return false;
    }
    
    public void flipAllImage(){                                     //hàm dùng để lật ngc các tấm hình lại
        
        for(int i=0;i<frameImages.size();i++){                      //lặp các tấm hình do frameImage nắm giữ
            
            BufferedImage image = frameImages.get(i).getImage();    //trích hình đó ra  //BufferedImage là lớp chuyên để làm việc với ảnh,
                                                                    //Lớp này lưu một mảng 2 chiều chứa thông tin của từng pixel trong ảnh
            
            AffineTransform tx = AffineTransform.getScaleInstance(-1,1);                    //lật hình//lớp này cho phép biến đổi theo các phép biến đổi
            tx.translate(-image.getWidth(),0);
            
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
            image = op.filter(image, null);
            
            frameImages.get(i).setImage(image);                     //set lai hinh sau khi lat
            
        }
    }
    
    public void draw(int x, int y, Graphics2D g2){              //ve ra hinh cua curr hien tai
        
        BufferedImage image = getCurrentImage();
        
        g2.drawImage(image, x - image.getWidth()/2, y - image.getHeight(), null);
        if(drawRectFrame)
            g2.drawRect(x - image.getWidth()/2, x - image.getWidth()/2, image.getWidth(), image.getHeight());       //ve ra 1 hinh chu nhat
    }
    
    
    
    
    
    
    
}
