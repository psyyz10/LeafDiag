function getDataFromImage(model)
c2c={'Natural' 'PowderyMildew' 'Septoria' 'Wheat rust'};

D{1}=dir([model '/Natural']);
D{2}=dir([model '/PowderyMildew']);
D{3}=dir([model '/Septoria']);
D{4}=dir([model '/Wheat rust']);
k=1;
for i=1:4
    CD=D{i};
    for j=1:length(CD)
        if length(strfind(CD(j).name,'.jpg'))>0
        %if(CD(j).bytes>0) && (~CD(j).isdir)
            pici=imread([model '/' c2c{i} '/' CD(j).name]); %read image
            bwi=im2bw(pici,0.1); %change to binary
            [r,c]=find(bwi==1); % find leaf or disease
            l4c(k).pic=pici;    % save color image
            l4c(k).bwc=bwi(min(r):max(r),min(c):max(c)); % save binary image
            l4c(k).picc=pici(min(r):max(r),min(c):max(c),:);  % save color image
            l4c(k).class=i-1;% mark class
            k=k+1;
        end
    end
end

l4c=createFeature(l4c,0); %create features

for i=1:length(l4c)
    train_data(i,:)=[l4c(i).Tamuratexture l4c(i).colorhist];
    %train_label{i}=c2cc{[l4c(i).class]+1};
    train_label(i)=l4c(i).class+1;
end
train_label=train_label';

save('leaf_data.mat','train_data','train_label');

%SupportVectorTrain(l4c); %train with SVM
%TreeBaggerClassic(l4c)% %train with Random Forest