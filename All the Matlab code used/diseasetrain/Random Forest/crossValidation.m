function [cvErr, confusion_matrix, RECALL, PRECISION, FMEASURE, aveRecall, avePrecision, aveFMeasure]= crossValidation(X,Y,k)

confusion_matrix = zeros(4,4);
CVO = cvpartition(Y,'k',k); % random partition the data into k foldss
err = zeros(CVO.NumTestSets,1);
RECALL = [];    % recall of classification results per fold
PRECISION = []; % precision of classification results per fold
FMEASURE = [];  % f-measure of classification results per fold
aveRecall = zeros(4,1);    % average recall of classification results
avePrecision = zeros(4,1); % average precision of classification results
aveFMeasure = zeros(4,1);  % average f-measure of classification results
output = [];
target = [];

for i = 1:CVO.NumTestSets           %for each fold
    trIdx = CVO.training(i);
    teIdx = CVO.test(i);
    %trees = learnDisease(X(trIdx,:), Y(trIdx,:)); %learn decision trees;
    %predictions = cellfun(@(x) str2diseaselable(diseaseDecision(x, trees)), num2cell(X(teIdx,:),2), 'UniformOutput', false);%calculate the preidction

    Factor = TreeBaggerClassic(X(trIdx,:), Y(trIdx,:));
    predictions =  cellfun(@str2num,predict(Factor, X(teIdx,:)));%calculate the preidction
    
    err(i) = sum(~(cellfun(@isequal, num2cell(predictions),num2cell(Y(teIdx,:),2)))); % compare with each real value to get the error    
    
    confusion_Matrix = confusionMatrix(Y(teIdx,:), predictions);            %calculate the confusion matrix, recall, precision, f measure below
    confusion_matrix = confusion_matrix + confusion_Matrix;
    output = [output; predictions];
    target = [target; Y(teIdx,:)];
    
    [recall, precision] = recallAndPrecision(confusion_Matrix);
    RECALL = [RECALL recall];
    PRECISION = [PRECISION precision];
    
    fmeasure = fMeasure(recall, precision);
    FMEASURE = [FMEASURE fmeasure];
end

target2 = zeros(4, size(target,1));
for i = 1: size(target,1)
    target2(target(i),i) = 1;
end

output2 = zeros(4, size(output,1));
for i = 1: size(output,1)
    output2(output(i),i) = 1;
end

plotconfusion(target2,output2);
cvErr = sum(err)/sum(CVO.TestSize);              %average errror

for j = 1 : 4
    aveRecall(j) = sum(RECALL(j,:)) /10;
    avePrecision(j) = sum(PRECISION(j,:)) /10;
    aveFMeasure(j) = sum(FMEASURE(j,:)) /10;
end


%[recall, precision] = recallAndPrecision(confusion_matrix);
%aveRecall = mean(RECALL);
%avePrecision = mean(PRECISION);
%aveFMeasure = mean(FMEASURE);