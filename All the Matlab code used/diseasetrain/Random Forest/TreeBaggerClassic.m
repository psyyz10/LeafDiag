%随机森林分类
function Factor = TreeBaggerClassic(train_data, train_label)

% 调用随机森林训练函数
Factor = TreeBagger(50,train_data,train_label,'oobpred','on');
plot(oobError(Factor));
xlabel('number of grown trees');
ylabel('out-of-bag classification error');


%利用训练好的随机森林进行分类
%predict(Factor,train_data(1,:))
%predict(Factor,train_data(2,:))
%predict(Factor,train_data)