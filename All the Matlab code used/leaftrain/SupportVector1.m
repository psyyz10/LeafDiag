function result =SupportVector1(svmStruct,sample)

% load svmStruct svmStruct;
ss=size(sample);
for ii=1:ss(1)
    num=zeros(1,4);
    for i=1:4
        for j=1:i-1
            G=svmclassify(svmStruct(i,j),sample(ii,:));
            if(G==1)
                num(i)=num(i)+1;
            elseif(G==-1)
                num(j)=num(j)+1;
            end
        end
    end
    [max_val,max_pos]=max(num);
    result(ii)=max_pos;
end

result=diseaselable2str(result);
result=result';