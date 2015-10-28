function [featureInd] = cfs(x,y)

featureInd = [];        % store indices of the features selected
indics = 1:136;         % candidate feature index set, used to remove features
matrix = corrcoef(x);   % the output of all the feature-feature correlation coefficient

while size(x,2) ~= 0
    k = length(indics); % the number of features left
    max_Ms = 0;
    for i = 1:k
        index_i = indics(i); % the actual index of dataset x
        current_feature = x(:,index_i); % the column of feature selected
        Rff = []; % the total rff
        for j = 1:k
            if(j~=i)
                index_j = indics(j);
                rff = matrix(index_i,index_j); % pick the specific correlation coefficient
                Rff = [Rff rff];
            end
        end
        
        aveRff = mean(Rff); % average of feature-feature correlation 
        Ccf = cov(current_feature, y);
        aveRcf = Ccf(2) / (std(current_feature)*std(y));
        current_Ms = (k * aveRcf) / (sqrt(k + k*(k-1)*aveRff)); % feature-class correlation coefficient
        
        if(current_Ms > max_Ms)
            max_Ms = current_Ms;
            index = i;
        end
    end
    
    if(index > i)
        break;
    else
        best_index = indics(index); % pick the actual best index of dataset x
        deletecol = false(1,k);
        deletecol(index) = true;
        indics(:,deletecol)=[];     % remove the index that has been picked
        featureInd = [featureInd best_index];
    end
end
