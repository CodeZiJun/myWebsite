import Docxtemplater from 'docxtemplater'
import PizZip from 'pizzip'
import JSZipUtils from 'jszip-utils'
import { saveAs } from 'file-saver'

export const exportDocx = (tempDocxPath, data, fileName) => {
    JSZipUtils.getBinaryContent(tempDocxPath, (error, content) => {
        if (error) {
            throw error
        }
        const zip = new PizZip(content)
        const doc = new Docxtemplater().loadZip(zip)
        console.log(doc)
        doc.setData(data)
        try {
            doc.render()
        } catch (error) {
            const e = {
                message: error.message,
                name: error.name,
                stack: error.stack,
                properties: error.properties
            }
            console.log({
                error: e
            })
            // The error thrown here contains additional information when logged with JSON.stringify (it contains a property object).
            throw error
        }
        const out = doc.getZip().generate({
            type: 'blob',
            mimeType: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
        }) // Output the document using Data-URI
        saveAs(out, fileName)
        return out
    }).then()
}