function GMM_test(filename,num)

Ima=imread(filename);
s=size(Ima);Ima=imresize(Ima,100/s(1));
[w,h,c]=size(Ima);
X=reshape(Ima,[w*h,c]);
% num=3; 
W=GMM_EM_3D(X,num); 

[maxp ind]=max(W,[],2);  

s = ones(length(X),1) * 3;
h = figure;
scatter3(X(:,1),X(:,2),X(:,3),s,ind);
saveas(h,'GMM', 'jpg');
