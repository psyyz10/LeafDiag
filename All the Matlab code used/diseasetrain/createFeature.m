function l4c=createFeature(l4c,figuredoor)
n=length(l4c);
for i=1:length(l4c)
    disp(['Extract features of ' num2str(i) ' training pictures, in totoal ' num2str(n) ' training pictures']);
    ch=colorhist(l4c(i).picc);
    ttt=Tamuratexture(l4c(i).picc);
    l4c(i).colorhist=ch;
    l4c(i).Tamuratexture=ttt;
    if figuredoor
        figure(1),
        subplot(2,2,1),imshow(l4c(i).picc);title([num2str(i) '-' num2str(l4c(i).class)])
        subplot(2,2,2),plot(ch),title(num2str(ttt));
        pause;
    end
end