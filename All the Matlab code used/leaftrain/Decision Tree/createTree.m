function T = createTree(T,X,label)

% T: current decision tree
% X: N*M training data matrix, each row is one sample
% label: N*1 labels of training data, each entry takes value 0 or 1
% 

% stop when data empty or all the data have some label
if isempty(X)
    T.class = majorityValue(label);
    return;
elseif all(label) == 1
    T.class = 1;
    return;
elseif all(1-label) == 1
    T.class = 0;
    return;
end;

max_X = max(X);
min_X = min(X);
splite_times = 10;
label_size = size(label,1);
best_gain = 0;

for feature = 1:size(X,2)
    lower_bound = max_X(feature);
    upper_bound = min_X(feature);
    threshold = linspace(lower_bound, upper_bound,splite_times + 2);    % linerly spaced 10 times between the upper and lower bound 
    threshold = threshold(2:end-1);                                     % get rid of the two extrem values
    
    for split = 1:splite_times                                          % cacluate gain for every threshold
        current_entropy = getEntropy(label);
        left_index = X(:,feature) <= threshold(split);
        right_index = X(:,feature) > threshold(split);
        
        left_label = label(left_index);
        right_label = label(right_index);
        
        left_size = size(left_label,1);
        right_size = size(right_label,1);
        
        remainder = left_size / label_size * getEntropy(left_label)...
            + right_size / label_size * getEntropy(right_label);
        
        current_gain = current_entropy - remainder; %ID3 algorithm
        
        if current_gain > best_gain       %get the best gain
            best_feature = feature;
            best_threshold = threshold(split);
            best_gain = current_gain;
        end;
    end;
end;

left_index = X(:,best_feature) <= best_threshold;
right_index = X(:,best_feature) > best_threshold;

T1 = createTree(T,X(left_index,:),label(left_index));
T2 = createTree(T,X(right_index,:),label(right_index));  

T.feature = best_feature;
T.threshold = best_threshold;
T.op = ['FEAT' num2str(best_feature) ' THRES' num2str(best_threshold)];
T.kids = {T1 T2};
