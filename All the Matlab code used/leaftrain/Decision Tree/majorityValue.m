function class = majorityValue(label)

if sum(label)  > 0.5 * size(label,1)
    class = 1;
else
    class = 0;
end;
