function [recall, precision] = recallAndPrecision(confusion_matrix)

recall = zeros(4,1);
precision = zeros(4,1);

for i = 1 : 4
    recall(i) = recallValue(confusion_matrix, i);
    precision(i) = precisionValue(confusion_matrix, i);
end



function recallValue = recallValue(confusion_matrix, i)
    TP = confusion_matrix(i,i);
    
    FN = 0;
    for j = 1:4
        if(j ~= i)
            FN = FN + confusion_matrix(i,j);
        end
    end
    
    if( (TP + FN) == 0)% no true positives & false negatives
        recallValue = 0;
    else
        recallValue = TP./(TP+FN);
    end
    
function precisionValue = precisionValue(confusion_matrix,i)
    TP = confusion_matrix(i,i);
    
    totalPositive = 0;
    for j = 1:4
        totalPositive = totalPositive + confusion_matrix(j,i);
    end
    
    if(totalPositive == 0)% no positive predict labels
        precisionValue = 0;
    else
        precisionValue = TP./totalPositive;
    end

