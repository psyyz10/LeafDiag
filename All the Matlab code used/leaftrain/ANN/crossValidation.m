function [cvErr, confusion_matrix, RECALL, PRECISION, FMEASURE, aveRecall, avePrecision, aveFMeasure] = crossValidation(x,y,k)

confusion_matrix = zeros(4,4);
CVO = cvpartition(y,'k',k); % random partition the data into k foldss
err = zeros(CVO.NumTestSets,1);
RECALL = [];    % recall of classification results per fold
PRECISION = []; % precision of classification results per fold
FMEASURE = [];  % f-measure of classification results per fold
aveRecall = zeros(4,1);    % average recall of classification results
avePrecision = zeros(4,1); % average precision of classification results
aveFMeasure = zeros(4,1);  % average f-measure of classification results
output = [];
target = [];

[x2, y2] = ANNdata(x,y); % data transformation

TF = {'tansig','purelin'};
BTF = 'trainlm';
BLF = 'learngdm';
PF = 'mse';

for i = 1:CVO.NumTestSets           %for each fold
    trIdx = CVO.training(i);
    teIdx = CVO.test(i);
    
    %net = newff([],[],10,TF,BTF,BLF,PF); % create network
    net = patternnet(30);
    
    %net.trainParam.show = 5;
    net.trainParam.epochs = 100;
    %net.trainParam.goal = 0.2;
    %net.trainParam.lr = 0.01;
    
    net = train(net,x2(:,trIdx), y2(:,trIdx)); % train the network
    predictionMatrix = sim(net,x2(:, teIdx));  % prediction results matrices of test data
    
    [value, predictions] = max(predictionMatrix,[],1); % final emotion predictions
    
    predictions = num2cell(predictions);
    err(i) = sum(~(cellfun(@isequal, predictions,num2cell(y(teIdx,:)')))); % compare with each real value to get the error
    predictions = cell2mat(predictions);
    
    test_y = y(teIdx,:)';
    confusion_Matrix = confusionMatrix(test_y, predictions);
    confusion_matrix = confusion_matrix + confusion_Matrix;
    output = [output; predictions'];
    target = [target; test_y'];
    
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

%plotroc(target2,output2)
plotconfusion(target2,output2);
cvErr = sum(err)/sum(CVO.TestSize); % average error

for j = 1 : 4
    aveRecall(j) = sum(RECALL(j,:)) /10;
    avePrecision(j) = sum(PRECISION(j,:)) /10;
    aveFMeasure(j) = sum(FMEASURE(j,:)) /10;
end
