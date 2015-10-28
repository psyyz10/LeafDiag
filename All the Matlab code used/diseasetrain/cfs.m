function [featureInd] = cfs(x,y)

featureInd = [];          % store indices of the features selected
corrMatrix = corrcoef([x,y]); % the output of all the feature-feature correlation coefficient
                          % and the feature-class correlation coefficient
final_Ms = 0;
current_Ms = 0;
s = size(x,2);

while current_Ms - final_Ms >= 0
    final_Ms = current_Ms;
    current_Ms = 0;
    index = 0;
    features = 1:s;
    features(featureInd) = [];
    k = length(featureInd) + 1;
    for i = features
        %x1 = [x(featureInd) x];
        
        aveRcf = mean(corrMatrix([featureInd i],s+1)); % the average of all the feature-class correlation
        aveRff = mean2(corrMatrix([featureInd i],[featureInd i]));
        % not sure about the aveRcf
        %aveRcf = corrMatrix(i,k+1); % correlation of the current feature and the class
        Ms = (k * aveRcf) / (sqrt(k + k*(k-1)*aveRff)); 
        
        if(Ms > current_Ms)
            current_Ms = Ms;
            index = i;
        end
    end
    
    if(index ~=0)
        featureInd = [featureInd index];
    end
end
