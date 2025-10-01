import { setupWorker } from "msw/browser";
import { handlers } from "./handlers";

const serverConfigurationObject = {
    onUnhandledRequest(request) {
        // avoid assets, fonts, images, or vite internal or auth middleware to be processed by mock server
        if (
            /(\/assets+)|(\/fonts+)|(\/images)|(\/__vite_ping)|(\/auth\/+)/.test(
                request.url.href
            )
        ) {
            return;
        }
    },
};

export const setupMockServer = async () => {
    if (import.meta.env.MODE === "localhost") {
        const worker = await setupWorker(...handlers);
        await worker.start(serverConfigurationObject);
    }
}
