function bw=keepGreenYellow(pic,bw)

[r,c]=find(bw==1);
for i=1:length(r)
        pixel =  pic(r(i),c(i),:);
        if pixel(3)> pixel(2)
            bw(r(i),c(i))=0;
        end
end