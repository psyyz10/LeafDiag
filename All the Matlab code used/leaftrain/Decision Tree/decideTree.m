function result = decideTree(x,T)

% x: one data sample, row vector
% T: decision tree
% predicate this sample

while isempty(T.class)
    if x(T.feature) <= T.threshold
        T = T.kids{1};
    else
        T = T.kids{2};
    end;
end;

result = T.class;