'use client'

import { ChangeEvent, FormEvent, useEffect, useMemo, useState } from 'react'
import { BarChart3, Box, CheckCircle2, ClipboardList, ImagePlus, LayoutDashboard, PackagePlus, Search, ShoppingBag, Trash2, Upload, X } from 'lucide-react'
import { toast, Toaster } from 'sonner'
import './styles.css'

type Product = { id: string; name: string; price: number; stock: number; image?: string; createdAt: string }
type FormValues = { name: string; price: string; stock: string; image?: string }
const STORAGE_KEY = 'dukaan-products'
const MAX_IMAGE_BYTES = 200 * 1024

const starterProducts: Product[] = [
  { id: 'starter-1', name: 'خالص شہد', price: 1850, stock: 24, createdAt: '2024-08-12' },
  { id: 'starter-2', name: 'کپاس کا دوپٹہ', price: 2400, stock: 8, createdAt: '2024-08-09' },
  { id: 'starter-3', name: 'ہاتھ کی بنی ٹوکری', price: 1250, stock: 16, createdAt: '2024-08-05' },
]

function readProducts(): Product[] {
  try {
    const stored = window.localStorage.getItem(STORAGE_KEY)
    return stored ? JSON.parse(stored) : starterProducts
  } catch { return starterProducts }
}

function compressImage(file: File): Promise<string | undefined> {
  return new Promise((resolve) => {
    const reader = new FileReader()
    reader.onload = () => {
      const image = new Image()
      image.onload = () => {
        const scale = Math.min(1, 1000 / Math.max(image.width, image.height))
        const canvas = document.createElement('canvas')
        canvas.width = Math.max(1, Math.round(image.width * scale))
        canvas.height = Math.max(1, Math.round(image.height * scale))
        const context = canvas.getContext('2d')
        if (!context) return resolve(undefined)
        context.drawImage(image, 0, 0, canvas.width, canvas.height)
        for (let quality = 0.82; quality >= 0.2; quality -= 0.08) {
          const result = canvas.toDataURL('image/jpeg', quality)
          if (result.length * 0.75 <= MAX_IMAGE_BYTES) return resolve(result)
        }
        resolve(undefined)
      }
      image.onerror = () => resolve(undefined)
      image.src = String(reader.result)
    }
    reader.onerror = () => resolve(undefined)
    reader.readAsDataURL(file)
  })
}

export default function Page() {
  const [products, setProducts] = useState<Product[]>([])
  const [form, setForm] = useState<FormValues>({ name: '', price: '', stock: '' })
  const [errors, setErrors] = useState<Record<string, string>>({})
  const [query, setQuery] = useState('')
  const [isSaving, setIsSaving] = useState(false)
  const [imageName, setImageName] = useState('')

  useEffect(() => setProducts(readProducts()), [])
  const filteredProducts = useMemo(() => products.filter((product) => product.name.toLowerCase().includes(query.toLowerCase())), [products, query])
  const totalStock = products.reduce((sum, product) => sum + product.stock, 0)

  function updateField(field: keyof FormValues, value: string) {
    setForm((current) => ({ ...current, [field]: value }))
    setErrors((current) => ({ ...current, [field]: '' }))
  }

  async function handleImage(event: ChangeEvent<HTMLInputElement>) {
    const file = event.target.files?.[0]
    if (!file) return
    const compressed = await compressImage(file)
    setImageName(compressed ? file.name : '')
    setForm((current) => ({ ...current, image: compressed }))
    if (!compressed) toast.info('تصویر 200 KB سے بڑی ہے، پروڈکٹ بغیر تصویر کے محفوظ ہوگی۔')
  }

  async function saveProduct(event: FormEvent) {
    event.preventDefault()
    const nextErrors: Record<string, string> = {}
    const name = form.name.trim()
    const price = Number(form.price)
    const stock = Number(form.stock)
    if (!name) nextErrors.name = 'پروڈکٹ کا نام درج کریں'
    if (!form.price || !Number.isFinite(price) || price <= 0) nextErrors.price = 'درست قیمت درج کریں'
    if (!form.stock || !Number.isInteger(stock) || stock < 0) nextErrors.stock = 'درست اسٹاک درج کریں'
    if (Object.keys(nextErrors).length) {
      setErrors(nextErrors)
      toast.error('براہ کرم فارم کی معلومات درست کریں۔')
      return
    }
    setIsSaving(true)
    const product: Product = { id: crypto.randomUUID(), name, price, stock, image: form.image, createdAt: new Date().toISOString() }
    const nextProducts = [product, ...products]
    try {
      window.localStorage.setItem(STORAGE_KEY, JSON.stringify(nextProducts))
      setProducts(nextProducts)
      setForm({ name: '', price: '', stock: '' })
      setImageName('')
      setErrors({})
      toast.success('پروڈکٹ کامیابی سے محفوظ ہوگئی۔')
    } catch {
      toast.error('پروڈکٹ محفوظ نہیں ہوسکی۔ دوبارہ کوشش کریں۔')
    } finally { setIsSaving(false) }
  }

  function removeProduct(id: string) {
    const next = products.filter((product) => product.id !== id)
    window.localStorage.setItem(STORAGE_KEY, JSON.stringify(next))
    setProducts(next)
    toast.success('پروڈکٹ حذف کردی گئی۔')
  }

  return <main dir="rtl" className="app-shell">
    <Toaster position="top-center" dir="rtl" richColors />
    <aside className="sidebar">
      <div className="brand"><div className="brand-mark"><ShoppingBag /></div><div><strong>دوکان</strong><span>Digital Kit</span></div></div>
      <nav><a className="nav-item active"><LayoutDashboard /> ڈیش بورڈ</a><a className="nav-item"><Box /> پروڈکٹس <span className="nav-count">{products.length}</span></a><a className="nav-item"><ClipboardList /> آرڈرز</a><a className="nav-item"><BarChart3 /> رپورٹس</a></nav>
      <div className="sidebar-footer"><div className="store-avatar">د</div><div><strong>میری دکان</strong><span>آن لائن</span></div><button aria-label="بند کریں"><X /></button></div>
    </aside>
    <section className="content-area">
      <header className="topbar"><div><p className="eyebrow">خوش آمدید، زین</p><h1>پروڈکٹس</h1></div><div className="top-actions"><span className="status-dot">● دکان فعال ہے</span><div className="profile">ز</div></div></header>
      <div className="content-grid">
        <section className="products-panel"><div className="section-heading"><div><h2>تمام پروڈکٹس</h2><p>اپنے اسٹور کے پروڈکٹس کا نظم کریں</p></div><div className="search-box"><Search /><input value={query} onChange={(e) => setQuery(e.target.value)} placeholder="پروڈکٹ تلاش کریں" aria-label="پروڈکٹ تلاش کریں" /></div></div>
          <div className="stats-row"><div className="stat-card"><span>کل پروڈکٹس</span><strong>{products.length}</strong><small>اسٹور میں موجود</small></div><div className="stat-card"><span>کل اسٹاک</span><strong>{totalStock}</strong><small>یونٹس دستیاب</small></div><div className="stat-card"><span>کم اسٹاک</span><strong>{products.filter((p) => p.stock < 10).length}</strong><small>دوبارہ منگوانا ہے</small></div></div>
          <div className="table-card"><div className="table-head"><span>پروڈکٹ</span><span>قیمت</span><span>اسٹاک</span><span>کارروائی</span></div>{filteredProducts.length ? filteredProducts.map((product) => <div className="product-row" key={product.id}><div className="product-cell"><div className="product-image">{product.image ? <img src={product.image} alt="" /> : <PackagePlus />}</div><div><strong>{product.name}</strong><small>شامل کیا گیا {new Date(product.createdAt).toLocaleDateString('ur-PK')}</small></div></div><span className="price">Rs. {product.price.toLocaleString('en-PK')}</span><span><b className={product.stock < 10 ? 'stock low' : 'stock'}>{product.stock}</b> یونٹس</span><button className="icon-button danger" onClick={() => removeProduct(product.id)} aria-label={`${product.name} حذف کریں`}><Trash2 /></button></div>) : <div className="empty-state"><PackagePlus /><strong>ابھی کوئی پروڈکٹ نہیں</strong><span>دائیں جانب فارم سے اپنی پہلی پروڈکٹ شامل کریں۔</span></div>}</div>
        </section>
        <section className="form-card"><div className="form-header"><div className="form-icon"><PackagePlus /></div><div><h2>نئی پروڈکٹ شامل کریں</h2><p>اپنی دکان میں نیا آئٹم شامل کریں</p></div></div><form onSubmit={saveProduct} noValidate><label className={errors.name ? 'has-error' : ''}>پروڈکٹ کا نام<input value={form.name} onChange={(e) => updateField('name', e.target.value)} placeholder="مثلاً: ہاتھ کی بنی ٹوکری" aria-invalid={!!errors.name} />{errors.name && <small>{errors.name}</small>}</label><div className="two-fields"><label className={errors.price ? 'has-error' : ''}>قیمت (روپے)<input type="number" min="1" value={form.price} onChange={(e) => updateField('price', e.target.value)} placeholder="0" aria-invalid={!!errors.price} />{errors.price && <small>{errors.price}</small>}</label><label className={errors.stock ? 'has-error' : ''}>اسٹاک<input type="number" min="0" value={form.stock} onChange={(e) => updateField('stock', e.target.value)} placeholder="0" aria-invalid={!!errors.stock} />{errors.stock && <small>{errors.stock}</small>}</label></div><label>تصویر <span className="optional">(اختیاری)</span><div className="upload-box"><input type="file" accept="image/*" onChange={handleImage} /><Upload /><span>{imageName || 'تصویر اپ لوڈ کرنے کے لیے کلک کریں'}</span><small>زیادہ سے زیادہ 200 KB</small></div></label><button className="save-button" type="submit" disabled={isSaving}>{isSaving ? 'محفوظ ہو رہا ہے...' : <><PackagePlus data-icon="inline-start" /> پروڈکٹ محفوظ کریں</>}</button><p className="form-note"><CheckCircle2 /> تصویر کے بغیر بھی پروڈکٹ محفوظ کی جاسکتی ہے</p></form></section>
      </div>
    </section>
  </main>
}
