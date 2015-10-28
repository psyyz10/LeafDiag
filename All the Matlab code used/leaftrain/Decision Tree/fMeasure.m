function fmeasure = fMeasure(recall, precision)
fmeasure = zeros(4,1);
for i = 1:4
    if(precision(i) == 0 && recall(i) == 0) 
        fmeasure(i) = 0;
    else
        fmeasure(i) = 2 * precision(i) * recall(i) / (precision(i) + recall(i));
    end
end