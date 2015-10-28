function oo = indirect_otsu(img)
I = img;

level1 = graythresh(I)
BW1 = im2bw(I,level1);

%imshow(BW1);

%hold on;
[rows,cols] = size(I);

%imshow(I);

%hold on;

%level2 = graythresh(I);
%BW2 = im2bw(I,level2);
%imshow(BW2);


for p=1:2
        
    I = uint8(BW1.*double(I));
    
    Ni=imhist(I);%计算直方图数组
    Ni(1) = 0;%去除不必要的信息。
    N=sum(Ni);   %总像素点个数
    delamax=0;   %类间方差最大值
    threshold=0; %阈值
    for k=2:255
        u=dot(0:255,Ni/N); %图像的总平均灰度级
        w0=sum(Ni(1:k)/N);  %C0类像素所占面积的比例
        w1=1-w0;            %C1类像素所占面积的比例
        if w0==0||w0==1      %当w0为1或0时提前结束本次循环
            continue
        end
        u0=dot(0:(k-1),Ni(1:k)/N)/w0;   %C0类像素的平均灰度
        u1=dot(k:255,Ni(k+1:256)/N)/w1; %C1类像素的平均灰度
        dela(k)=w0*(u-u0)^2+w1*(u-u1)^2;  %类间方差公式
        %求出类间方差的最大值，最大时的那个值对应的k值存入delamax
        if dela(k)>delamax
            delamax=dela(k);
            threshold=k-1;
        end
    end
    %以下为阈值分割程序
    if p == 2
        t2 = threshold;
    end
    if p == 3
        t3 = threshold;
        %	threshold = t2+(t3-t2)*0;
    end
    
    
    
    
    
    [width,height]=size(I);%获取图片宽高
    for i=1:width
        for j=1:height
            if(I(i,j)<threshold)%灰度小于阈值时则为黑色
                BW1(i,j)=0;
            else
                BW1(i,j)=1; % 灰度大于阈值时则为白色
            end
        end
    end
    
    if i == 2
        t2 = threshold;
    end
    
    
    if i == 3
        t3 = threshold;
    end
    
    %figure
    %hold on;
    %imshow(BW1),title('自编程序运行结果')%显示图片
    
end
oo = BW1;
%figure
%imshow(BW1),title('自编程序运行结果')%显示图片