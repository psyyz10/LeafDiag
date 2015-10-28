function emotion = diseaseDecision(x,trees)
%%
% x: one data sample, row vector
% trees: an array of six decision trees;
% 
%%

result=zeros(64,1);
idx = 1;


% for every two emotions, create a tree, so there are 15 trees in these
% case
for i=1:4                                   %i reperesents the first emotion
    for j=i+1:4                             %j reperesents the second emotion
        if decideTree(x,trees(idx))
            result(i) = result(i) + 1;      %if the it returns a positive value, put a vote for the first emotion 
        else 
            result(j) = result(j) + 1;      %if the it returns a negative value, put a vote for the first emotion 
        end
        idx = idx+1;
    end
end

[value index] = max(result);                % get the emotion which has the most vote
emotion= diseaselable2str(index); 


