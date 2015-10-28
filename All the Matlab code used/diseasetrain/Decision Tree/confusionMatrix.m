function [matrix] = confusionMatrix(actual, predictions)
% produces a 6x6 confusion matrix when given a vector of the actual values
% and a vector of the values after classification

matrix = zeros(4);
for i = 1: size(actual,1);
    % count the ones have been predicted as some kind of emotion
    matrix(actual(i), predictions(i)) = matrix(actual(i), predictions(i)) + 1;    
end

