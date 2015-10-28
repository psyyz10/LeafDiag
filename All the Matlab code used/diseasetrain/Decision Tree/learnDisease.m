function trees = learnDisease(x,y)
%%
% input x is emotion data matrix, y is emotion lables
% ouput y is six decision trees as an vector
%%

trees = [];

for i=1:4 % for every combination of the two emotions
    for j=i+1:4
        trees = [trees decisionTree_learning(x(y==i|y==j,:),y(y==i|y==j,:)==i)]; %concatenate 15 trees classificaiton results
    end
end
