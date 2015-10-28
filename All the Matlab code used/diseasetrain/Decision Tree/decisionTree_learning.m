function T = decisionTree_learning(X,label);
%%
%output T: return a tree.
%input: X is an emotion matrix
%input: label is an emotion label

%%

T = struct('op',[],'kids',[],'class',[],'feature',0,'threshold',0);

T = createTree(T,X,label);