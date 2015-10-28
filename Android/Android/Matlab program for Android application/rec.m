function rec(filename,num)
%load the training model
%load TreeBagger leaftrain/Factor; %load random forest model
%load leaftrain/svmStruct svmStruct;%load SVM model
load TreeBagger;%load SVM model


%segment to get leaves, if without leaf, prompt no leaf in the picture
Ima=imread(filename);
s=size(Ima);
Ima=imresize(Ima,500/s(1));%zoom the picture into the width of 500
%imshow(Ima);
[w,h,c]=size(Ima);
X=reshape(Ima,[w*h,c]); %data structure transformation, transform to the sample data which can be used by function GMM_EM_3D
% num=3; %classification number
W=GMM_EM_3D(X,num); %EM algorithm parameter estimation
sw=size(W);
if sw(2)<num
    return;
end
for i=1:num
    mask(:,:,i)=reshape(W(:,i),[w,h]); %data structure transformation
end
[maxp ind]=max(mask,[],3); %find every pixel with most possibility from which Gaussian distribution

leafpicflag=0;
minother=20;
%hFig=figure(1);
for i=1:num
    pic=uint8(repmat(ind==i,[1 1 c]).*double(Ima));
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
    %subplot(2,2,i),
    %imshow(uint8(repmat(ind==i,[1 1 c]).*double(Ima)))
    %display segmentation result
    %title(['The results of segmentation via GMM model and EM algorithm: part ' num2str(i)]);
end


if leafpicflag==1
    h = figure(2);
    %subplot(2,1,1);
    %imshow(Ima);title('original picture');
    bw=bwareaopen(bw,50);
    pic=uint8(repmat(bw,[1 1 c]).*double(Ima));
    bw=keepGreenYellow(pic,bw);
    
    %subplot(2,1,2);
    imshow(uint8(repmat(imfill(bw,'holes'),[1 1 c]).*double(Ima)));title('recognition result');
    
    [B,L]=bwlabel(bw);
    k=1;
    for j=1:L
        leaf(k).pic=uint8(repmat(imfill(B==j,'holes'),[1 1 3]).*double(Ima));
        bw=im2bw(leaf(k).pic,0.001);
        s=size(bw);
        area=s(1)*s(2);
        area_rate=sum(bw(:))/area*100;
        if area_rate>0.1
            rgb=[sum(sum(leaf(k).pic,1),2)];
            rgb=rgb./rgb(2);
            if rgb(1)<1.1 && rgb(3)<0.855 %guarentee the segmentation region is leaf
                leaf(k).bw=imfill(B==j,'holes');
                
                %                 leaf(k).class=0;
                leaf(k).name=[ num2str(j) '-' filename];
                bwi=leaf(k).bw;
                pici=leaf(k).pic;
                [r,c]=find(bwi==1);
                leaf(k).bwc=bwi(min(r):max(r),min(c):max(c));
                leaf(k).picc=pici(min(r):max(r),min(c):max(c),:);
                
                ch=colorhist(leaf(k).picc);%extract features from every leaf, enter recognition program to do recognition
                ttt=Tamuratexture(leaf(k).picc);
                %imshow(leaf(k).picc);
                leaf(k).colorhist=ch;
                leaf(k).Tamuratexture=ttt;
                %svm_result(j)=SupportVector1(svmStruct,[ttt ch]); %recognition with SVM
                rf_result(j)=predict(Factor,[ttt ch]);%recognition with RF
                [r,c]=find(leaf(k).bw==1);
                
                %text(ceil(mean(c)),ceil(mean(r)),svm_result(j),'Color','red','FontSize',18);
                text(ceil(mean(c)),ceil(mean(r)),diseaselable2str(str2double(rf_result(j))),'Color','red','FontSize',15);
                rectangle('position',[min(c) min(r) max(c)-min(c) max(r)-min(r)],'EdgeColor','red' );
                k=k+1;
            else
                leaf(k).bw=(B==j);
                leaf(k).name=[ num2str(j) '-' filename];
                [r,c]=find(leaf(k).bw==1);
                %svm_result(j)={'no leaf'};
                rf_result(j)={'no leaf'};
                text(ceil(mean(c)),ceil(mean(r)),[rf_result(j) num2str(rgb(3))],'Color','yellow','FontSize',18);
                rectangle('position',[min(c) min(r) max(c)-min(c) max(r)-min(r)],'EdgeColor','yellow' );
            end
        end
    end
    
else
    %no leaf in picture
    disp('no leaf segamented from the picture, you can try to change the segamentation parameter');
    svm_result_c='no leaf in picture, you can try to change the segamentation parameter';
    h=figure(2);imshow(Ima);title(svm_result_c);
end

ti = get(gca,'TightInset')
set(gca,'Position',[ti(1) ti(2) 1-ti(3)-ti(1) 1-ti(4)-ti(2)]);
set(gca,'units','centimeters')
pos = get(gca,'Position');
ti = get(gca,'TightInset');

set(gcf, 'PaperUnits','centimeters');
set(gcf, 'PaperSize', [pos(3)+ti(1)+ti(3) pos(4)+ti(2)+ti(4)]);
set(gcf, 'PaperPositionMode', 'manual');
set(gcf, 'PaperPosition',[0 0 pos(3)+ti(1)+ti(3) pos(4)+ti(2)+ti(4)]);
print(h,'-dpng','/Users/mac/NetBeansProjects/WebApplication1/web/identification');


