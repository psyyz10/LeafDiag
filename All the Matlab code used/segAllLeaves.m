function segAllLeaves(folder_path)

c2c={'Natural' 'Powdery Mildew' 'Septoria' 'Wheat rust'};

D{1}=dir([folder_path '/Natural']);
D{2}=dir([folder_path '/Powdery Mildew']);
D{3}=dir([folder_path '/Septoria']);
D{4}=dir([folder_path '/Wheat rust']);
k=0;
for i=1:4
    CD=D{i};
    for j=1:length(CD)
        if ~isempty(strfind(CD(j).name,'.png'))
            %if(CD(j).bytes>0) && (~CD(j).isdir)
            k1 =segToLeaf([folder_path '/' c2c{i} '/' CD(j).name],2,k);
            k = k1 + k;
        end
    end
end

