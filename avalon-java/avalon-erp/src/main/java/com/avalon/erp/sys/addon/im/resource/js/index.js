(async () => {
    console.log("hello, im");

    window.ws = new ReconnectingWebSocket('/ws')
})();