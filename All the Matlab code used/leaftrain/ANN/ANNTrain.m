function [err] = ANNTrain(x,y)
% this function used to produce a trained network
% using 2/3 dataset as training set to train the network
% and 1/3 dataset as testing set to optimize the network parameters

TF = {'tansig','purelin'};
BTF = 'trainlm';
BLF = 'learngdm';
PF = 'mse';


CVO = cvpartition(y,'k',3);
trIdx = CVO.training(1);
teIdx = CVO.test(1);

[x2 y2] = ANNdata(x,y); % data transformation

net = newff([],[],20,TF,BTF,BLF,PF);

%net.trainParam.show = 5;
net.trainParam.epochs = 100;
%net.trainParam.goal = 0.2;
%net.trainParam.lr = 0.01;

[net tr]= train(net, x2(:,trIdx), y2(:,trIdx)); % train the network

predictionMatrix = sim(net,x2(:, teIdx)); % prediction results matrices of test data

[value, predictions] = max(predictionMatrix,[],1); % final emotion predictions

predictions = num2cell(predictions);
err = sum(~(cellfun(@isequal, predictions,num2cell(y(teIdx,:)')))); % compare with each real value to get the error
err = err/length(y(teIdx,:));

save('net.mat', 'net');

