function entropy = getEntropy(label_set)

positive = sum(label_set);
negative = sum(1-label_set);

if positive + negative == 0
    entropy = 0;
    return;
end;

p_p = positive/(positive + negative);
p_n = negative/(positive + negative);

if positive == 0
    entropy = - p_n * log2(p_n);
    return;
end;

if negative == 0
    entropy = - p_p * log2(p_p);
    return;
end;

entropy = - p_n * log2(p_n) - p_p * log2(p_p);

