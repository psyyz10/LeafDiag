function k = segToLeaf(filename,num, startName)

numLeaf = 0;
Ima=imread(filename);
s=size(Ima);Ima=imresize(Ima,500/s(1));
[w,h,c]=size(Ima);
X=reshape(Ima,[w*h,c]);
% num=3; 
W=GMM_EM_3D(X,num); 
sw=size(W);
if sw(2)<num 
    return;
end

%reshape the prior possiblity to image format
for i=1:num
    mask(:,:,i)=reshape(W(:,i),[w,h]); 
end

%decide which categority has the most probablity for each pixel data
[maxp ind]=max(mask,[],3);  

leafpicflag=0;
minother=20;
for i=1:num
    pic=uint8(repmat(ind==i,[1 1 3]).*double(Ima)); %
    %figure;
    %imshow(pic);
    rgb=[sum(sum(pic,1),2)];
    rgb=rgb./max(rgb);
    if rgb(2)>rgb(1) && rgb(2)>rgb(3)
        if minother>rgb(1)+rgb(3)
            leafpicflag=1;
            minother=rgb(1)+rgb(3);
            leafpic=pic;
            bw=(ind==i);
        end
    end
end
k=1;
if leafpicflag==1
    bw=bwareaopen(bw,50);
    pic=uint8(repmat(bw,[1 1 c]).*double(Ima));
    bw=keepGreenYellow(pic,bw);
    %figure;
    %imshow(uint8(repmat(imfill(bw,'holes'),[1 1 c]).*double(Ima)));title('recognition result');
    
    [B,L]=bwlabel(bw);
    for j=1:L
        leaf(k).pic=uint8(repmat(imfill(B==j,'holes'),[1 1 3]).*double(Ima));
        bw=im2bw(leaf(k).pic,0.001);
        s=size(bw);
        area=s(1)*s(2);
        area_rate=sum(bw(:))/area*100;
        if area_rate>0.1
            rgb=[sum(sum(leaf(k).pic,1),2)];
            rgb=rgb./rgb(2);
            if rgb(1)<1.1 && rgb(3)<0.855 
                leaf(k).bw=imfill(B==j,'holes');
                leaf(k).name=[ num2str(j) '-' filename];
                bwi=leaf(k).bw;
                pici=leaf(k).pic;
                [r,c]=find(bwi==1);
                leaf(k).bwc=bwi(min(r):max(r),min(c):max(c));
                leaf(k).picc=pici(min(r):max(r),min(c):max(c),:);
                %t = Tamuratexture(leaf(k).picc);
                imwrite(leaf(k).picc,['Leaf/' num2str(startName+k) '.tif']);
                %tt = Tamuratexture(imread(['Leaf/' num2str(startName+k) '.tif']));
                k=k+1;
            else
                leaf(k).bw=(B==j);
                leaf(k).name=[ num2str(j) '-' filename];
            end
        end
    end
    
    
else
    %图片中没有叶子
    segToLeaf(filename,num+1, startName);
end

k=k-1;
