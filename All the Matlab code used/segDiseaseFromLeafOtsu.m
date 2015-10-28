function segDiseaseFromLeafOtsu(leaf_model)

c2c={'Natural' 'PowderyMildew' 'Septoria' 'Wheat rust'};

D{1}=dir([leaf_model '/Natural']);
D{2}=dir([leaf_model '/PowderyMildew']);
D{3}=dir([leaf_model '/Septoria']);
D{4}=dir([leaf_model '/Wheat rust']);
k = 0;
for i=1:4
    CD=D{i};
    for j=1:length(CD)
        if ~isempty(strfind(CD(j).name,'.tif'))
            %if(CD(j).bytes>0) && (~CD(j).isdir)
            pici=imread([leaf_model '/' c2c{i} '/' CD(j).name]);
            gray=rgb2gray(pici);
            oo = indirect_otsu(gray);
            direct_leaf = bwfilter(pici,oo);
            imwrite(direct_leaf,['Disease/' num2str(i) '_' num2str(k) '_disease.tif']);
            k = k + 1;
        end
    end
end

