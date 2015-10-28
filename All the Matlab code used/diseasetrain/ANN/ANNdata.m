function [x2, y2] = ANNdata(x, y)
x2 = x';
y2 = zeros(4, size(y,1));
for i = 1: size(y,1)
    y2(y(i),i) = 1;
end