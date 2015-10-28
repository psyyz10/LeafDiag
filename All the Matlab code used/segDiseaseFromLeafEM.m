function segDiseaseFromLeafEM(filename,num)

Ima=imread(filename);
s=size(Ima);Ima=imresize(Ima,500/s(1));
%I = rgb2hsv(Ima);
[w,h,c]=size(Ima);
X=reshape(Ima,[w*h,c]); 
% num=3; 
W=GMM_EM_3D(X,num); 
sw=size(W);
if sw(2)<num 
    return;
end
for i=1:num
    mask(:,:,i)=reshape(W(:,i),[w,h]);
end
[maxp ind]=max(mask,[],3);

h=figure;

subplot(3,1,1);
pic=uint8(repmat(ind==1,[1 1 3]).*double(Ima));
imshow(pic);

subplot(3,1,2);
pic=uint8(repmat(ind==2,[1 1 3]).*double(Ima));
imshow(pic);

subplot(3,1,3);
pic=uint8(repmat(ind==3,[1 1 3]).*double(Ima));
imshow(pic);

saveas(h,'em','png');
